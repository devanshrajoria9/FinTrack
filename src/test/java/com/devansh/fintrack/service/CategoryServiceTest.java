package com.devansh.fintrack.service;

import com.devansh.fintrack.dto.request.CreateCategoryRequestDto;
import com.devansh.fintrack.dto.response.CategoryResponseDto;
import com.devansh.fintrack.entity.Category;
import com.devansh.fintrack.entity.User;
import com.devansh.fintrack.enums.CategoryType;
import com.devansh.fintrack.exception.ResourceNotFoundException;
import com.devansh.fintrack.repository.CategoryRepository;
import com.devansh.fintrack.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CategoryService categoryService;

    @AfterEach
    void testDown(){
        SecurityContextHolder.clearContext();
    }
    @Test
    void createCategory_shouldCreateCategorySuccessfully() {

        User user = new User();
        user.setId(1L);
        user.setEmail("devansh@gmail.com");

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("devansh@gmail.com", null)
        );

        when(userRepository.findByEmail("devansh@gmail.com")).thenReturn(Optional.of(user));

        CreateCategoryRequestDto request =
                new CreateCategoryRequestDto(
                        "Food",
                        "Food expenses",
                        CategoryType.EXPENSE
                );

        when(categoryRepository.save(any(Category.class))).thenAnswer(invocation -> {
            Category category = invocation.getArgument(0);
            category.setId(10L);
            return category;
        });

        CategoryResponseDto response =
                categoryService.createCategory(request);

        assertNotNull(response);
        assertEquals(10L, response.getId());
        assertEquals("Food", response.getName());
        assertEquals("Food expenses", response.getDescription());
        assertEquals(CategoryType.EXPENSE, response.getType());

        verify(userRepository).findByEmail("devansh@gmail.com");
        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    void createCategory_shouldThrowExceptionWhenUserNotFound() {

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        "devanshraj@gmail.com", null
                )
        );

        when(userRepository.findByEmail("devanshraj@gmail.com"))
                .thenReturn(Optional.empty());

        CreateCategoryRequestDto request =
                new CreateCategoryRequestDto(
                        "Food",
                        "Food expenses",
                        CategoryType.EXPENSE
                );

        assertThrows(
                ResourceNotFoundException.class,
                () -> categoryService.createCategory(request)
        );

        verify(categoryRepository, never()).save(any(Category.class));
    }
    @Test
    void getCategory_shouldReturnCategoryWhenFound() {
        User user = new User();
        user.setId(1L);
        user.setEmail("devansh@gmail.com");

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        "devansh@gmail.com", null
                )
        );

        Category category = new Category();
        category.setId(10L);
        category.setName("Food");
        category.setDescription("Food expenses");
        category.setType(CategoryType.EXPENSE);
        category.setUser(user);

        when(userRepository.findByEmail("devansh@gmail.com"))
                .thenReturn(Optional.of(user));
        when(categoryRepository.findByIdAndUserId(10L, 1L))
                .thenReturn(Optional.of(category));

        CategoryResponseDto response = categoryService.getCategory(10L);

        assertNotNull(response);
        assertEquals(10L, response.getId());
        assertEquals("Food", response.getName());

        verify(categoryRepository).findByIdAndUserId(10L, 1L);
    }

    @Test
    void getCategory_shouldThrowExceptionWhenNotFound() {
        User user = new User();
        user.setId(1L);
        user.setEmail("devansh@gmail.com");

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        "devansh@gmail.com", null
                )
        );

        when(userRepository.findByEmail("devansh@gmail.com"))
                .thenReturn(Optional.of(user));
        when(categoryRepository.findByIdAndUserId(99L, 1L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> categoryService.getCategory(99L)
        );
    }

    @Test
    void getCategory_shouldNotReturnAnotherUsersCategory() {
        User user = new User();
        user.setId(1L);
        user.setEmail("devansh@gmail.com");

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        "devansh@gmail.com", null
                )
        );

        when(userRepository.findByEmail("devansh@gmail.com"))
                .thenReturn(Optional.of(user));
        when(categoryRepository.findByIdAndUserId(10L, 1L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> categoryService.getCategory(10L)
        );

        verify(categoryRepository).findByIdAndUserId(10L, 1L);
    }
}
