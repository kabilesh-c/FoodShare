package com.example.foodshare.config;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * Global exception handler for the application
 * Handles errors gracefully and provides user-friendly error pages
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ModelAndView handleAccessDenied(AccessDeniedException ex, HttpServletRequest request) {
        log.error("Access Denied: {}", ex.getMessage());
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("errorTitle", "Access Denied");
        mav.addObject("errorMessage", "You don't have permission to access this resource.");
        mav.addObject("errorCode", "403");
        return mav;
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ModelAndView handleAuthenticationException(AuthenticationException ex, HttpServletRequest request) {
        log.error("Authentication error: {}", ex.getMessage());
        return new ModelAndView("redirect:/login?error=true");
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView handleNotFound(NoHandlerFoundException ex, HttpServletRequest request) {
        log.error("Page not found: {}", request.getRequestURI());
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("errorTitle", "Page Not Found");
        mav.addObject("errorMessage", "The page you're looking for doesn't exist.");
        mav.addObject("errorCode", "404");
        return mav;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ModelAndView handleIllegalArgument(IllegalArgumentException ex, HttpServletRequest request) {
        log.error("Invalid argument: {}", ex.getMessage());
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("errorTitle", "Invalid Request");
        mav.addObject("errorMessage", ex.getMessage());
        mav.addObject("errorCode", "400");
        return mav;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView handleGenericException(Exception ex, HttpServletRequest request) {
        log.error("Unexpected error: ", ex);
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("errorTitle", "Something Went Wrong");
        mav.addObject("errorMessage", "An unexpected error occurred. Please try again later.");
        mav.addObject("errorCode", "500");
        mav.addObject("errorDetails", ex.getMessage());
        return mav;
    }
}
