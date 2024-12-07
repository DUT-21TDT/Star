package com.pbl.star.utils;

import com.pbl.star.dtos.query.user.OnSuggestionProfile;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomSuggest {
    public static List<OnSuggestionProfile> weightedRandom(List<OnSuggestionProfile> suggestions, int numToSelect) {
        List<OnSuggestionProfile> selected = new ArrayList<>();
        List<OnSuggestionProfile> suggestionsCopy = new ArrayList<>(suggestions);
        Random random = new Random();

        while (selected.size() < numToSelect && !suggestionsCopy.isEmpty()) {
            // Tính tổng điểm
            int totalScore = suggestionsCopy.stream().mapToInt(OnSuggestionProfile::getTotalRelationScore).sum();
            // Random một số từ 0 đến totalScore
            int rand = random.nextInt(totalScore);

            // Tìm người dựa vào điểm tích lũy
            int cumulative = 0;
            OnSuggestionProfile chosen = null;
            for (OnSuggestionProfile person : suggestionsCopy) {
                cumulative += person.getTotalRelationScore();
                if (rand < cumulative) {
                    chosen = person;
                    break;
                }
            }

            // Thêm vào danh sách kết quả và loại khỏi danh sách nguồn
            if (chosen != null) {
                selected.add(chosen);
                suggestionsCopy.remove(chosen);
            }
        }

        return selected;
    }
}
