package com.electricalstore.electricalstore.controllers;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class SetErrorController implements ErrorController {
    @RequestMapping(value = "/error", method = { RequestMethod.GET, RequestMethod.POST })
    public ModelAndView renderErrorPage(HttpServletRequest httpRequest) {
        ModelAndView errorPage = new ModelAndView("error");
        Integer httpErrorCode = getErrorCode(httpRequest);
        String errorMessage = switch (httpErrorCode) {
            case 400 -> "Recurso inexistente.";
            case 403 -> "No tiene permisos para acceder al recurso.";
            case 401 -> "No se encuentra autorizado";
            case 404 -> "El recurso solicitado no fue encontrado";
            case 500 -> "Ocurrio un error interno";
            default -> "Se produjo un error inesperado";
        };
        errorPage.addObject("errorCode", httpErrorCode);
        errorPage.addObject("errorMessage", errorMessage);
        return errorPage;
    }

    public String redirect() {
        return "redirect: /error";
    }

    public Integer getErrorCode(HttpServletRequest httpRequest) {
        Object statusCode = httpRequest.getAttribute("jakarta.servlet.error.status_code");
        if (statusCode instanceof Integer) {
            return (Integer) statusCode;
        }
        return -1;
    }

}