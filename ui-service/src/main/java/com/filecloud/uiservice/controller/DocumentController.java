package com.filecloud.uiservice.controller;

import com.filecloud.uiservice.client.response.DocumentResponse;
import com.filecloud.uiservice.client.response.SpaceInfoResponse;
import com.filecloud.uiservice.constant.UiConst;
import com.filecloud.uiservice.dto.mvcmodel.DocumentModel;
import com.filecloud.uiservice.dto.session.UserSession;
import com.filecloud.uiservice.response.Result;
import com.filecloud.uiservice.service.DocumentService;
import com.filecloud.uiservice.service.UserService;
import com.filecloud.uiservice.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.TreeMap;

@RequestMapping("/documents")
@Controller
public class DocumentController extends BaseController {

    private final DocumentService documentService;

    @Autowired
    public DocumentController(UserService userService, DocumentService documentService) {
        this.documentService = documentService;
    }

    @GetMapping(value = {"", "/"})
    public String home(HttpSession session, Model model, @ModelAttribute(UiConst.KEY_RESULT_MESSAGE) String resultMessage, @ModelAttribute(UiConst.KEY_ERROR) String errorMessage) {
        UserSession currentUser = getVerifiedUser(session);
        model.addAttribute("documents", documentService.listDocuments(getBearerToken(currentUser)));
        model.addAttribute(UiConst.KEY_ERROR, (errorMessage != null && errorMessage.equals("")) ? null : errorMessage);
        model.addAttribute(UiConst.KEY_RESULT_MESSAGE, (resultMessage != null && resultMessage.equals("")) ? null : resultMessage);
        model.addAttribute(UiConst.KEY_USER, currentUser);
        return "document/documents";
    }

    @GetMapping("/space-info")
    public String spaceInfo(HttpSession session, Model model, @ModelAttribute(UiConst.KEY_RESULT_MESSAGE) String resultMessage, @ModelAttribute(UiConst.KEY_ERROR) String errorMessage) {
        UserSession currentUser = getVerifiedUser(session);
        SpaceInfoResponse spaceInfo = documentService.spaceInfo(getBearerToken(currentUser));

        Map<String, Double> graphData = new TreeMap<>();
        graphData.put("Used Space", Util.roundUptoTwo(Util.convertBytesToMb(spaceInfo.getUsedSpace())));
        graphData.put("Remaining Space", Util.roundUptoTwo(Util.convertBytesToMb(spaceInfo.getRemainingSpace())));

        model.addAttribute("chartData", graphData);
        model.addAttribute("spaceLimit", Util.roundUptoTwo(Util.convertBytesToMb(spaceInfo.getSpaceLimit())) + " MB");
        model.addAttribute(UiConst.KEY_ERROR, (errorMessage != null && errorMessage.equals("")) ? null : errorMessage);
        model.addAttribute(UiConst.KEY_RESULT_MESSAGE, (resultMessage != null && resultMessage.equals("")) ? null : resultMessage);
        model.addAttribute(UiConst.KEY_USER, currentUser);
        return "document/space-info";
    }

    @GetMapping(value = "/{id}")
    public String getDocument(@PathVariable long id, HttpSession session, Model model) {
        UserSession currentUser = getVerifiedUser(session);
        Result<DocumentResponse> result = documentService.getDocument(getBearerToken(currentUser), id);

        if (!result.isSuccess()) {
            model.addAttribute("result", "ERROR");
            model.addAttribute(UiConst.KEY_ERROR, result.getMessage());
        } else {
            model.addAttribute("result", "SUCCESS");
            model.addAttribute("document", new DocumentModel(result.getData()));
        }

        model.addAttribute(UiConst.KEY_USER, currentUser);
        return "document/document";
    }

}
