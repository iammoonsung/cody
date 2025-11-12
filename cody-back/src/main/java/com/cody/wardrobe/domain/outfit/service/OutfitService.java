package com.cody.wardrobe.domain.outfit.service;

import com.cody.wardrobe.domain.history.History;
import com.cody.wardrobe.domain.history.repository.HistoryRepository;
import com.cody.wardrobe.domain.item.Item;
import com.cody.wardrobe.domain.item.repository.ItemRepository;
import com.cody.wardrobe.domain.outfit.Outfit;
import com.cody.wardrobe.domain.outfit.OutfitItem;
import com.cody.wardrobe.domain.outfit.dto.OutfitCreateDto;
import com.cody.wardrobe.domain.outfit.dto.OutfitDto;
import com.cody.wardrobe.domain.outfit.dto.OutfitUpdateDto;
import com.cody.wardrobe.domain.outfit.repository.OutfitItemRepository;
import com.cody.wardrobe.domain.outfit.repository.OutfitRepository;
import com.cody.wardrobe.exception.ServiceException;
import com.cody.wardrobe.exception.ServiceExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OutfitService {

    private final OutfitRepository outfitRepository;
    private final OutfitItemRepository outfitItemRepository;
    private final ItemRepository itemRepository;
    private final HistoryRepository historyRepository;

    /**
     * 코디 등록
     */
    @Transactional
    public OutfitDto createOutfit(OutfitCreateDto dto) {
        // 아이템 검증
        if (dto.getItemIds() == null || dto.getItemIds().isEmpty()) {
            throw new ServiceException(ServiceExceptionCode.OUTFIT_ITEMS_REQUIRED);
        }

        // 코디 생성
        Outfit outfit = Outfit.createWithDetails(
                dto.getName(),
                dto.getRating(),
                dto.getFormalityLevel(),
                dto.getMemo()
        );

        // 저장 (ID 생성)
        Outfit savedOutfit = outfitRepository.save(outfit);

        // 아이템들 추가
        for (Long itemId : dto.getItemIds()) {
            Item item = itemRepository.findById(itemId)
                    .orElseThrow(() -> new ServiceException(
                            ServiceExceptionCode.ITEM_NOT_FOUND, "ID: " + itemId));

            OutfitItem outfitItem = OutfitItem.create(savedOutfit, item);
            savedOutfit.addItem(outfitItem);
        }

        return OutfitDto.from(savedOutfit);
    }

    /**
     * 코디 조회
     */
    public OutfitDto getOutfit(Long id) {
        Outfit outfit = outfitRepository.findById(id)
                .orElseThrow(() -> new ServiceException(
                        ServiceExceptionCode.OUTFIT_NOT_FOUND, "ID: " + id));
        return OutfitDto.from(outfit);
    }

    /**
     * 모든 코디 조회
     */
    public List<OutfitDto> getAllOutfits() {
        return outfitRepository.findAll().stream()
                .map(OutfitDto::from)
                .collect(Collectors.toList());
    }

    /**
     * 평점 기준 코디 조회
     */
    public List<OutfitDto> getOutfitsByRating(Integer minRating) {
        return outfitRepository.findByRatingGreaterThanEqual(minRating).stream()
                .map(OutfitDto::from)
                .collect(Collectors.toList());
    }

    /**
     * 격식도 기준 코디 조회
     */
    public List<OutfitDto> getOutfitsByFormalityLevel(Integer minFormality, Integer maxFormality) {
        return outfitRepository.findByFormalityLevelBetween(minFormality, maxFormality).stream()
                .map(OutfitDto::from)
                .collect(Collectors.toList());
    }

    /**
     * 코디 추천 (기본)
     */
    public List<OutfitDto> recommendOutfits(Integer minRating, Integer minFormality) {
        return outfitRepository.findByRatingAndFormalityForRecommendation(minRating, minFormality).stream()
                .map(OutfitDto::from)
                .collect(Collectors.toList());
    }

    /**
     * 코디 추천 (최근 착용 제외)
     */
    public List<OutfitDto> recommendOutfitsExcludingRecent(
            Integer minRating, Integer minFormality, Integer excludeDays) {
        LocalDate excludeAfter = LocalDate.now().minusDays(excludeDays);
        return outfitRepository.findByRatingAndFormalityExcludingRecent(
                minRating, minFormality, excludeAfter).stream()
                .map(OutfitDto::from)
                .collect(Collectors.toList());
    }

    /**
     * 코디 수정
     */
    @Transactional
    public OutfitDto updateOutfit(Long id, OutfitUpdateDto dto) {
        // 코디 조회
        Outfit outfit = outfitRepository.findById(id)
                .orElseThrow(() -> new ServiceException(
                        ServiceExceptionCode.OUTFIT_NOT_FOUND, "ID: " + id));

        // 아이템 검증
        if (dto.getItemIds() == null || dto.getItemIds().isEmpty()) {
            throw new ServiceException(ServiceExceptionCode.OUTFIT_ITEMS_REQUIRED);
        }

        // 기본 정보 업데이트
        outfit.update(dto.getName(), dto.getRating(), dto.getFormalityLevel(), dto.getMemo());

        // 기존 아이템들 제거
        outfit.clearItems();

        // 새 아이템들 추가
        for (Long itemId : dto.getItemIds()) {
            Item item = itemRepository.findById(itemId)
                    .orElseThrow(() -> new ServiceException(
                            ServiceExceptionCode.ITEM_NOT_FOUND, "ID: " + itemId));

            OutfitItem outfitItem = OutfitItem.create(outfit, item);
            outfit.addItem(outfitItem);
        }

        return OutfitDto.from(outfit);
    }

    /**
     * 코디 착용 기록
     */
    @Transactional
    public void recordWorn(Long id, LocalDate date) {
        Outfit outfit = outfitRepository.findById(id)
                .orElseThrow(() -> new ServiceException(
                        ServiceExceptionCode.OUTFIT_NOT_FOUND, "ID: " + id));

        // 중복 확인 (같은 날짜에 같은 코디를 이미 기록했는지)
        if (historyRepository.existsByOutfitIdAndWornDate(id, date)) {
            // 이미 기록되어 있으면 outfit만 업데이트
            outfit.recordWorn(date);
            return;
        }

        // History 생성
        History history = History.create(outfit, date);
        historyRepository.save(history);

        // Outfit 정보 업데이트 (lastWornDate, wornCount)
        outfit.recordWorn(date);
    }

    /**
     * 코디 삭제
     */
    @Transactional
    public void deleteOutfit(Long id) {
        // 코디 존재 확인
        if (!outfitRepository.existsById(id)) {
            throw new ServiceException(
                    ServiceExceptionCode.OUTFIT_NOT_FOUND, "ID: " + id);
        }

        // 삭제 (cascade로 OutfitItem도 함께 삭제됨)
        outfitRepository.deleteById(id);
    }
}
