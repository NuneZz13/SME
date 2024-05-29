package pt.at.sme.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import pt.at.sme.InitialValues;
import pt.at.sme.kotlin.priorNotification.*;

/**
 * Single page controller that returns the view index.html.
 * This view contains react-router that will render the page associated to the desired url mapping.
 */
@Controller
class SinglePageController {


    /**
     * Single page application endpoint:
     * <ul>
     * <li>this is the only request mapping in a @Controller</li>
     * <li>the actual body of the pages is under control of react-router (see main.tsx)</li>
     * <li>as new endpoints are added to the router, add here as well</li>
     * </ul>
     *
     * @return View index.html
     */
    @RequestMapping({
            "", "/",
            "/classic",
            "/hooks",
            "/kformPersonalInfoForm",
            "/kform", "/kform/*",
    })
    public String singlePage(Model model) {
        PriorNotificationForm prefilledForm = null;
//        prefilledForm = new PriorNotificationForm(
//                new Identification(
//                        new VatIdentificationNumber("PT", "501303260"),
//                        "Nome Cadastro",
//                        Arrays.asList("Codigo CAE Cadastro 1, Codigo CAE Cadastro 2"),
//                        "",
//                        false,
//                        null),
//                new Contact("Rua Cadastro", "Numero Cadastro", "Código postal Cadastro", "Cidade Cadastro", "PT",null,"999999999", "geral@opensoft.pt"),
//                null,
//                new Table<TotalValueOfSuppliesModel>(
//                        Collections.singletonList(
//                                new TotalValueOfSuppliesModel(
//                                        "PT",
//                                        0
//                                )
//                        )),
//                null,
//                null,
//                false
//        );



        InitialValues initialValues = new InitialValues("501303260");
        model.addAttribute("initialValues", "501303260");
        return "/index.html";

    }

}
