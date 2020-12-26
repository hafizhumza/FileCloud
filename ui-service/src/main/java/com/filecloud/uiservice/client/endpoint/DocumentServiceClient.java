package com.filecloud.uiservice.client.endpoint;

import com.filecloud.uiservice.client.request.IdRequest;
import com.filecloud.uiservice.client.request.ShareDocumentRequest;
import com.filecloud.uiservice.client.request.UpdateRequest;
import com.filecloud.uiservice.client.response.DocumentResponse;
import com.filecloud.uiservice.client.response.SingleFieldResponse;
import com.filecloud.uiservice.client.response.SpaceInfoResponse;
import com.filecloud.uiservice.constant.UiConst;
import com.filecloud.uiservice.response.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@FeignClient(contextId = "DocumentService", name = "GatewayServer", path = UiConst.URL_DOCUMENT_SERVICE)
public interface DocumentServiceClient {

    // TODO: to be fixed
    @PostMapping("/upload")
    Result<?> upload(@RequestHeader("Authorization") String bearerToken, @RequestParam MultipartFile document, @RequestParam String properties);

    @GetMapping("/download/{documentId}")
    ResponseEntity<ByteArrayResource> download(@RequestHeader("Authorization") String bearerToken, @PathVariable long documentId);

    @GetMapping("/{documentId}")
    Result<?> getDocument(@RequestHeader("Authorization") String bearerToken, @PathVariable long documentId);

    @PostMapping("/update")
    Result<?> update(@RequestHeader("Authorization") String bearerToken, @RequestBody UpdateRequest request);

    @PostMapping("/delete")
    Result<?> delete(@RequestHeader("Authorization") String bearerToken, @RequestBody IdRequest request);

    @PostMapping("/share")
    Result<?> share(@RequestHeader("Authorization") String bearerToken, @RequestBody ShareDocumentRequest request);

    @GetMapping("/list")
    Result<List<DocumentResponse>> listDocuments(@RequestHeader("Authorization") String bearerToken);

    @GetMapping("/space-info")
    Result<SpaceInfoResponse> spaceInfo(@RequestHeader("Authorization") String bearerToken);

    @GetMapping("/count")
    Result<SingleFieldResponse> count(@RequestHeader("Authorization") String bearerToken);

}
