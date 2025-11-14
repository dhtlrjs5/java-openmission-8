package com.maple.mapleinfo.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static com.maple.mapleinfo.utils.ErrorMessages.ERROR_GENERAL_EXCEPTION;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CharacterNotFoundException.class)
    public String handleCharacterNotFound(CharacterNotFoundException e, Model model) {
        model.addAttribute("message", e.getMessage());
        return "error";
    }

    @ExceptionHandler(Exception.class)
    public String handleGeneralException(Model model) {
        model.addAttribute("message", ERROR_GENERAL_EXCEPTION);
        return "error";
    }
}
