package com.cody.wardrobe.domain.item.service;

import com.cody.wardrobe.domain.item.Item;
import com.cody.wardrobe.domain.item.ItemCategory;
import com.cody.wardrobe.domain.item.Season;
import com.cody.wardrobe.domain.item.dto.ItemCreateDto;
import com.cody.wardrobe.domain.item.dto.ItemDto;
import com.cody.wardrobe.domain.item.dto.ItemUpdateDto;
import com.cody.wardrobe.domain.item.repository.ItemRepository;
import com.cody.wardrobe.exception.ServiceException;
import com.cody.wardrobe.exception.ServiceExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemService {

    private final ItemRepository itemRepository;

    /**
     * 아이템 등록
     */
    @Transactional
    public ItemDto createItem(ItemCreateDto dto) {
        // 아이템 생성
        Item item = Item.createWithDetails(
                dto.getCategory(),
                dto.getImageUrl(),
                dto.getName(),
                dto.getColor(),
                dto.getSeason()
        );

        // 저장
        Item savedItem = itemRepository.save(item);
        return ItemDto.from(savedItem);
    }

    /**
     * 아이템 조회
     */
    public ItemDto getItem(Long id) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new ServiceException(
                        ServiceExceptionCode.ITEM_NOT_FOUND, "ID: " + id));
        return ItemDto.from(item);
    }

    /**
     * 모든 아이템 조회
     */
    public List<ItemDto> getAllItems() {
        return itemRepository.findAll().stream()
                .map(ItemDto::from)
                .collect(Collectors.toList());
    }

    /**
     * 카테고리별 아이템 조회
     */
    public List<ItemDto> getItemsByCategory(ItemCategory category) {
        return itemRepository.findByCategory(category).stream()
                .map(ItemDto::from)
                .collect(Collectors.toList());
    }

    /**
     * 계절별 아이템 조회
     */
    public List<ItemDto> getItemsBySeason(Season season) {
        return itemRepository.findBySeason(season).stream()
                .map(ItemDto::from)
                .collect(Collectors.toList());
    }

    /**
     * 카테고리와 계절로 아이템 조회
     */
    public List<ItemDto> getItemsByCategoryAndSeason(ItemCategory category, Season season) {
        return itemRepository.findByCategoryAndSeason(category, season).stream()
                .map(ItemDto::from)
                .collect(Collectors.toList());
    }

    /**
     * 이름으로 아이템 검색
     */
    public List<ItemDto> searchItemsByName(String name) {
        return itemRepository.findByNameContainingIgnoreCase(name).stream()
                .map(ItemDto::from)
                .collect(Collectors.toList());
    }

    /**
     * 아이템 수정
     */
    @Transactional
    public ItemDto updateItem(Long id, ItemUpdateDto dto) {
        // 아이템 조회
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new ServiceException(
                        ServiceExceptionCode.ITEM_NOT_FOUND, "ID: " + id));

        // 아이템 업데이트
        item.update(
                dto.getCategory(),
                dto.getImageUrl(),
                dto.getName(),
                dto.getColor(),
                dto.getSeason()
        );

        return ItemDto.from(item);
    }

    /**
     * 아이템 삭제
     */
    @Transactional
    public void deleteItem(Long id) {
        // 아이템 존재 확인
        if (!itemRepository.existsById(id)) {
            throw new ServiceException(
                    ServiceExceptionCode.ITEM_NOT_FOUND, "ID: " + id);
        }

        // 삭제
        itemRepository.deleteById(id);
    }
}
