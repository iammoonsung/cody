package com.cody.wardrobe.domain.history.service;

import com.cody.wardrobe.domain.history.History;
import com.cody.wardrobe.domain.history.dto.HistoryCreateDto;
import com.cody.wardrobe.domain.history.dto.HistoryDto;
import com.cody.wardrobe.domain.history.repository.HistoryRepository;
import com.cody.wardrobe.domain.outfit.Outfit;
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
public class HistoryService {

    private final HistoryRepository historyRepository;
    private final OutfitRepository outfitRepository;

    /**
     * 착용 기록 등록
     */
    @Transactional
    public HistoryDto createHistory(HistoryCreateDto dto) {
        // 코디 조회
        Outfit outfit = outfitRepository.findById(dto.getOutfitId())
                .orElseThrow(() -> new ServiceException(
                        ServiceExceptionCode.OUTFIT_NOT_FOUND, "ID: " + dto.getOutfitId()));

        // 중복 확인
        if (historyRepository.existsByOutfitIdAndWornDate(dto.getOutfitId(), dto.getWornDate())) {
            throw new ServiceException(ServiceExceptionCode.DUPLICATE_HISTORY);
        }

        // 착용 기록 생성
        History history = History.create(outfit, dto.getWornDate());

        // 코디의 착용 정보 업데이트
        outfit.recordWorn(dto.getWornDate());

        // 저장
        History savedHistory = historyRepository.save(history);
        return HistoryDto.from(savedHistory);
    }

    /**
     * 착용 기록 단건 조회
     */
    public HistoryDto getHistory(Long id) {
        History history = historyRepository.findById(id)
                .orElseThrow(() -> new ServiceException(
                        ServiceExceptionCode.HISTORY_NOT_FOUND, "ID: " + id));
        return HistoryDto.from(history);
    }

    /**
     * 모든 착용 기록 조회 (최신순)
     */
    public List<HistoryDto> getAllHistories() {
        return historyRepository.findRecentHistory().stream()
                .map(HistoryDto::from)
                .collect(Collectors.toList());
    }

    /**
     * 특정 코디의 착용 기록 조회
     */
    public List<HistoryDto> getHistoriesByOutfit(Long outfitId) {
        // 코디 존재 확인
        if (!outfitRepository.existsById(outfitId)) {
            throw new ServiceException(
                    ServiceExceptionCode.OUTFIT_NOT_FOUND, "ID: " + outfitId);
        }

        return historyRepository.findByOutfitId(outfitId).stream()
                .map(HistoryDto::from)
                .collect(Collectors.toList());
    }

    /**
     * 날짜 범위로 착용 기록 조회
     */
    public List<HistoryDto> getHistoriesByDateRange(LocalDate startDate, LocalDate endDate) {
        return historyRepository.findByWornDateBetweenOrderByWornDateDesc(startDate, endDate).stream()
                .map(HistoryDto::from)
                .collect(Collectors.toList());
    }

    /**
     * 특정 월의 착용 기록 조회
     */
    public List<HistoryDto> getHistoriesByMonth(int year, int month) {
        return historyRepository.findByYearAndMonth(year, month).stream()
                .map(HistoryDto::from)
                .collect(Collectors.toList());
    }

    /**
     * 착용 기록 삭제
     */
    @Transactional
    public void deleteHistory(Long id) {
        // 착용 기록 조회
        History history = historyRepository.findById(id)
                .orElseThrow(() -> new ServiceException(
                        ServiceExceptionCode.HISTORY_NOT_FOUND, "ID: " + id));

        Outfit outfit = history.getOutfit();

        // 삭제
        historyRepository.deleteById(id);

        // Outfit wornCount 감소
        outfit.decrementWornCount();

        // lastWornDate 재계산
        List<History> remainingHistories = historyRepository.findByOutfitId(outfit.getId());
        if (remainingHistories.isEmpty()) {
            outfit.updateLastWornDate(null);
        } else {
            LocalDate lastDate = remainingHistories.stream()
                    .map(History::getWornDate)
                    .max(LocalDate::compareTo)
                    .orElse(null);
            outfit.updateLastWornDate(lastDate);
        }
    }
}
