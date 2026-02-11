package com.github.common.util;

import com.github.common.exception.InvalidCSVException;
import com.github.common.exception.InvalidIdException;
import com.github.common.exception.MetadataValidationException;
import com.github.common.model.SongMetadata;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Stream;


@RequiredArgsConstructor
public class Validator {

    public Long mapIdToNumber(String id) {
        if (StringUtils.isNumeric(id)) {
            try {
                long l = Long.parseLong(id);
                if (l == 0) {
                    throw new InvalidIdException(String.format("Invalid value '%s' for ID. Must be a positive integer", id));
                }
                return l;
            } catch (NumberFormatException e) {
                throw new InvalidIdException(String.format("Invalid value '%s' for ID. Must be a positive integer", id));
            }
        }
        throw new InvalidIdException(String.format("Invalid value '%s' for ID. Must be a positive integer", id));
    }

    public List<Long> flatMapCsv(String ids) {
        int length = StringUtils.length(ids);
        if (length > 200) {
            throw new InvalidCSVException(String.format("CSV string is too long: received %s characters, maximum allowed is 200", length));
        }
        return Stream.of(ids.split(","))
                .map(this::mapIdToNumber2)
                .toList();
    }

    private Long mapIdToNumber2(String id) {
        if (StringUtils.isNumeric(id)) {
            try {
                long l = Long.parseLong(id);
                if (l == 0) {
                    throw new InvalidIdException(String.format("Invalid ID format: '%s'. Only positive integers are allowed", id));
                }
                return l;
            } catch (NumberFormatException e) {
                throw new InvalidIdException(String.format("Invalid ID format: '%s'. Only positive integers are allowed", id));
            }
        }
        throw new InvalidIdException(String.format("Invalid ID format: '%s'. Only positive integers are allowed", id));
    }

    public void validate(SongMetadata song) {
        Map<String, String> errors = new HashMap<>();

        if (song.getId() == null) {
            errors.put("id", "Id is required.");
        }
//        else if (!songRepository.existsById(song.getId())) {
//            errors.add("id does not match an existing resource.");
//        }
        if (isNullOrEmpty(song.getName())) {
            errors.put("name", "Song name is required");
        } else if (song.getName().length() > 100) {
            errors.put("name", "Song name must be between 1 and 100 characters");
        }

        if (isNullOrEmpty(song.getArtist())) {
            errors.put("artist", "Artist name is required");
        } else if (song.getArtist().length() > 100) {
            errors.put("artist", "Artist name must be between 1 and 100 characters");
        }

        if (isNullOrEmpty(song.getAlbum())) {
            errors.put("album", "Album name is required");
        } else if (song.getAlbum().length() > 100) {
            errors.put("album", "Album name must be between 1 and 100 characters");
        }

        if (isNullOrEmpty(song.getDuration())) {
            errors.put("duration", "Duration is required");
        } else if (!Pattern.matches("^\\d{2}:\\d{2}$", song.getDuration())) {
            errors.put("duration", "Duration must be in mm:ss format with leading zeros");
        } else {
            String[] parts = song.getDuration().split(":");
            int minutes = Integer.parseInt(parts[0]);
            int seconds = Integer.parseInt(parts[1]);
            if (minutes < 0 || minutes > 59 || seconds < 0 || seconds > 59) {
                errors.put("duration", "Duration must be in mm:ss format with leading zeros");
            }
        }

        if (isNullOrEmpty(song.getYear())) {
            errors.put("year", "Year is required");
        } else if (!Pattern.matches("^(19|20)\\d{2}$", song.getYear())) {
            errors.put("year", "Year must be between 1900 and 2099");
        }
        if (!errors.isEmpty()) {
            throw new MetadataValidationException("Validation error", errors);
        }

    }

    private boolean isNullOrEmpty(String s) {
        return s == null || s.trim().isEmpty();
    }
}
