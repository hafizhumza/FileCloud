package com.filecloud.uiservice.controller;

import com.filecloud.uiservice.client.request.DocumentUpdateRequest;
import com.filecloud.uiservice.client.response.DocumentResponse;
import com.filecloud.uiservice.client.response.SpaceInfoResponse;
import com.filecloud.uiservice.constant.UiConst;
import com.filecloud.uiservice.dto.mvcmodel.DocumentModel;
import com.filecloud.uiservice.dto.mvcmodel.DocumentUploadModel;
import com.filecloud.uiservice.dto.mvcmodel.DownloadDocumentModel;
import com.filecloud.uiservice.dto.session.UserSession;
import com.filecloud.uiservice.exception.RecordNotFoundException;
import com.filecloud.uiservice.response.Result;
import com.filecloud.uiservice.service.DocumentService;
import com.filecloud.uiservice.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Map;
import java.util.TreeMap;

@RequestMapping("/documents")
@Controller
public class DocumentController extends BaseController {

    private final DocumentService documentService;

    @Autowired
    public DocumentController(DocumentService documentService) {
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
        model.addAttribute("spaceLimit", Util.humanReadableByteCountBin(spaceInfo.getSpaceLimit()));
        model.addAttribute(UiConst.KEY_ERROR, (errorMessage != null && errorMessage.equals("")) ? null : errorMessage);
        model.addAttribute(UiConst.KEY_RESULT_MESSAGE, (resultMessage != null && resultMessage.equals("")) ? null : resultMessage);
        model.addAttribute(UiConst.KEY_USER, currentUser);
        return "document/space-info";
    }

    @GetMapping("/{id}")
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

    @GetMapping("/upload")
    public String upload(HttpSession session,
                         Model model,
                         @ModelAttribute(UiConst.KEY_RESULT_MESSAGE) String resultMessage,
                         @ModelAttribute(UiConst.KEY_ERROR) String errorMessage) {

        UserSession currentUser = getVerifiedUser(session);
        model.addAttribute("uploadModel", new DocumentUploadModel());
        model.addAttribute(UiConst.KEY_ERROR, (errorMessage != null && errorMessage.equals("")) ? null : errorMessage);
        model.addAttribute(UiConst.KEY_RESULT_MESSAGE, (resultMessage != null && resultMessage.equals("")) ? null : resultMessage);
        model.addAttribute(UiConst.KEY_USER, currentUser);
        return "document/upload";
    }

    @PostMapping("/upload")
    public String upload(@Valid @ModelAttribute DocumentUploadModel uploadModel,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes,
                         HttpSession session, Model model,
                         @ModelAttribute(UiConst.KEY_RESULT_MESSAGE) String resultMessage,
                         @ModelAttribute(UiConst.KEY_ERROR) String errorMessage) {

        UserSession currentUser = getVerifiedUser(session);
        model.addAttribute(UiConst.KEY_USER, currentUser);

        if (bindingResult.hasErrors() || (uploadModel.getDocument() == null || uploadModel.getDocument().isEmpty())) {
            model.addAttribute("uploadModel", uploadModel);

            if (uploadModel.getDocument() == null || uploadModel.getDocument().isEmpty())
                model.addAttribute(UiConst.KEY_ERROR, "Please select a document");
            else
                model.addAttribute(UiConst.KEY_ERROR, bindingResult.getFieldErrors().get(0).getDefaultMessage());

            return "document/upload";
        }

        Result<DocumentResponse> result = documentService.upload(getBearerToken(currentUser), uploadModel);

        if (!result.isSuccess()) {
            model.addAttribute("uploadModel", uploadModel);
            model.addAttribute(UiConst.KEY_ERROR, result.getMessage());
            return "document/upload";
        } else {
            redirectAttributes.addFlashAttribute(UiConst.KEY_RESULT_MESSAGE, "Document saved successfully");
        }

        return "redirect:/documents";
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<ByteArrayResource> download(@PathVariable long id, HttpSession session, Model model) {
        UserSession currentUser = getVerifiedUser(session);
        DownloadDocumentModel dto = documentService.download(getBearerToken(currentUser), id);

        if (dto == null || dto.getResource() == null)
            throw new RecordNotFoundException();

        return ResponseEntity.ok()
                .headers(dto.getHeaders())
                .contentLength(dto.getResource().contentLength())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(dto.getResource());
    }

    @GetMapping("/update/{id}")
    public String update(@PathVariable long id,
                         HttpSession session,
                         Model model,
                         @ModelAttribute(UiConst.KEY_RESULT_MESSAGE) String resultMessage,
                         @ModelAttribute(UiConst.KEY_ERROR) String errorMessage) {

        UserSession currentUser = getVerifiedUser(session);
        Result<DocumentResponse> result = documentService.getDocument(getBearerToken(currentUser), id);

        if (!result.isSuccess())
            throw new RecordNotFoundException();

        DocumentResponse documentResponse = result.getData();

        DocumentUpdateRequest request = new DocumentUpdateRequest();
        request.setDocumentId(documentResponse.getDocumentId());
        request.setName(documentResponse.getDisplayName());
        request.setDescription(documentResponse.getDescription());

        model.addAttribute("document", request);
        model.addAttribute(UiConst.KEY_ERROR, (errorMessage != null && errorMessage.equals("")) ? null : errorMessage);
        model.addAttribute(UiConst.KEY_RESULT_MESSAGE, (resultMessage != null && resultMessage.equals("")) ? null : resultMessage);
        model.addAttribute(UiConst.KEY_USER, currentUser);
        return "document/update";
    }

    @PostMapping("/update")
    public String update(@Valid @ModelAttribute DocumentUpdateRequest updateRequest,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes,
                         HttpSession session, Model model,
                         @ModelAttribute(UiConst.KEY_RESULT_MESSAGE) String resultMessage,
                         @ModelAttribute(UiConst.KEY_ERROR) String errorMessage) {

        UserSession currentUser = getVerifiedUser(session);
        model.addAttribute(UiConst.KEY_USER, currentUser);

        if (bindingResult.hasErrors()) {
            model.addAttribute("document", updateRequest);
            model.addAttribute(UiConst.KEY_ERROR, bindingResult.getFieldErrors().get(0).getDefaultMessage());
            return "document/update";
        }

        Result<DocumentResponse> result = documentService.update(getBearerToken(currentUser), updateRequest);

        if (!result.isSuccess()) {
            model.addAttribute("uploadModel", updateRequest);
            model.addAttribute(UiConst.KEY_ERROR, result.getMessage());
            return "document/update";
        } else {
            redirectAttributes.addFlashAttribute(UiConst.KEY_RESULT_MESSAGE, result.getMessage());
        }

        return "redirect:/documents";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable long id,
                         HttpSession session,
                         RedirectAttributes redirectAttributes) {

        UserSession currentUser = getVerifiedUser(session);
        Result<String> result = documentService.delete(getBearerToken(currentUser), id);

        if (!result.isSuccess())
            redirectAttributes.addFlashAttribute(UiConst.KEY_ERROR, result.getMessage());
        else
            redirectAttributes.addFlashAttribute(UiConst.KEY_RESULT_MESSAGE, result.getMessage());

        return "redirect:/documents";
    }

}
