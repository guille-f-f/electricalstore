package com.electricalstore.electricalstore.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ErrorControlador implements ErrorController {
    @RequestMapping(value = "/error", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView renderErrorPage(HttpServletRequest httpRequest) {
        ModelAndView errorPage = new ModelAndView("error");
        Integer httpErrorCode = getErrorCode(httpRequest);
        String errorMsg = switch (httpErrorCode) {
            case 400 -> "Recurso inexistente.";
            case 403 -> "No tiene permisos para acceder al recurso.";
            case 401 -> "No se encuentra autorizado";
            case 404 -> "El recurso solicitado no fue encontrado";
            case 500 -> "Ocurrio un error interno";
            default -> "Se produjo un error inesperado";
        };
        errorPage.addObject("codigo", httpErrorCode);
        errorPage.addObject("mensaje", errorMsg);
        return errorPage;
    }

    private Integer getErrorCode(HttpServletRequest httpRequest) {
        Object statusCode = httpRequest.getAttribute("jakarta.servlet.error.status_code");
        if (statusCode instanceof Integer) {
            return (Integer) statusCode;
        }
        return -1;
    }

    // Ya no es necesario desde Spring Boot 2.3 en adelante. Antes se usaba para especificar la ruta del error.
    // En versiones nuevas es ignorado, pero podés dejarlo si querés por compatibilidad.
    public String getErrorPath() {
        return "/error.html";
    }
}
