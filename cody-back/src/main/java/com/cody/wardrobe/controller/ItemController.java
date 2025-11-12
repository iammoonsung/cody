package com.cody.wardrobe.controller;

import com.cody.wardrobe.common.ApiResponse;
import com.cody.wardrobe.controller.dto.item.ItemRequest;
import com.cody.wardrobe.controller.dto.item.ItemResponse;
import com.cody.wardrobe.domain.item.ItemCategory;
import com.cody.wardrobe.domain.item.Season;
import com.cody.wardrobe.domain.item.dto.ItemCreateDto;
import com.cody.wardrobe.domain.item.dto.ItemDto;
import com.cody.wardrobe.domain.item.dto.ItemUpdateDto;
import com.cody.wardrobe.domain.item.service.ItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Item", description = "옷장 아이템 관리 API")
@RestController
@RequestMapping("/api/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @Operation(summary = "아이템 등록", description = "새로운 옷장 아이템을 등록합니다.")
    @PostMapping
    public ResponseEntity<ApiResponse<ItemResponse>> createItem(@Valid @RequestBody ItemRequest request) {
        // Request → Service DTO 변환
        ItemCreateDto createDto = ItemCreateDto.from(request);

        // Service 호출
        ItemDto itemDto = itemService.createItem(createDto);

        // Service DTO → Response 변환
        ItemResponse response = ItemResponse.from(itemDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(response));
    }

    @Operation(summary = "아이템 단건 조회", description = "ID로 아이템을 조회합니다.")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ItemResponse>> getItem(@PathVariable Long id) {
        ItemDto itemDto = itemService.getItem(id);
        ItemResponse response = ItemResponse.from(itemDto);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "모든 아이템 조회", description = "모든 옷장 아이템을 조회합니다.")
    @GetMapping
    public ResponseEntity<ApiResponse<List<ItemResponse>>> getAllItems() {
        List<ItemDto> items = itemService.getAllItems();
        List<ItemResponse> responses = items.stream()
                .map(ItemResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(responses));
    }

    @Operation(summary = "카테고리별 아이템 조회", description = "특정 카테고리의 아이템을 조회합니다.")
    @GetMapping("/category/{category}")
    public ResponseEntity<ApiResponse<List<ItemResponse>>> getItemsByCategory(
            @Parameter(description = "아이템 카테고리") @PathVariable ItemCategory category) {
        List<ItemDto> items = itemService.getItemsByCategory(category);
        List<ItemResponse> responses = items.stream()
                .map(ItemResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(responses));
    }

    @Operation(summary = "계절별 아이템 조회", description = "특정 계절의 아이템을 조회합니다.")
    @GetMapping("/season/{season}")
    public ResponseEntity<ApiResponse<List<ItemResponse>>> getItemsBySeason(
            @Parameter(description = "계절") @PathVariable Season season) {
        List<ItemDto> items = itemService.getItemsBySeason(season);
        List<ItemResponse> responses = items.stream()
                .map(ItemResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(responses));
    }

    @Operation(summary = "이름으로 아이템 검색", description = "이름에 키워드가 포함된 아이템을 검색합니다.")
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<ItemResponse>>> searchItems(
            @Parameter(description = "검색 키워드") @RequestParam String name) {
        List<ItemDto> items = itemService.searchItemsByName(name);
        List<ItemResponse> responses = items.stream()
                .map(ItemResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(responses));
    }

    @Operation(summary = "아이템 수정", description = "아이템 정보를 수정합니다.")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ItemResponse>> updateItem(
            @PathVariable Long id,
            @Valid @RequestBody ItemRequest request) {
        // Request → Service DTO 변환
        ItemUpdateDto updateDto = ItemUpdateDto.from(request);

        // Service 호출
        ItemDto itemDto = itemService.updateItem(id, updateDto);

        // Service DTO → Response 변환
        ItemResponse response = ItemResponse.from(itemDto);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "아이템 삭제", description = "아이템을 삭제합니다.")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteItem(@PathVariable Long id) {
        itemService.deleteItem(id);
        return ResponseEntity.ok(ApiResponse.success());
    }
}
