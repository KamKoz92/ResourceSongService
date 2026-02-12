package com.github.resource.util;


import com.github.resource.exception.InvalidCSVException;import com.github.resource.exception.InvalidIdException;import com.github.resource.exception.MetadataValidationException;import com.github.resource.model.SongMetadata;import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Stream;


@RequiredArgsConstructor
public class ResourceHelper {

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
                .map(this::mapCvsIdToNumber)
                .toList();
    }

    private Long mapCvsIdToNumber(String id) {
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

    public void validateSongMetadata(SongMetadata metadata) {
        Map<String, String> errors = new HashMap<>();

        if (metadata.getId() == null) {
            errors.put("id", "Id is required.");
        }

        if (metadata.getName() == null) {
            errors.put("name", "Song name is required");
        } else if (isNullOrEmpty(metadata.getName()) || metadata.getName().length() > 100) {
            errors.put("name", "Song name must be between 1 and 100 characters");
        }

        if (metadata.getArtist() == null) {
            errors.put("artist", "Artist name is required");
        } else if (isNullOrEmpty(metadata.getArtist()) || metadata.getArtist().length() > 100) {
            errors.put("artist", "Artist name must be between 1 and 100 characters");
        }

        if (metadata.getAlbum() == null) {
            errors.put("album", "Album name is required");
        } else if (isNullOrEmpty(metadata.getAlbum()) || metadata.getAlbum().length() > 100) {
            errors.put("album", "Album name must be between 1 and 100 characters");
        }

        if (metadata.getDuration() == null) {
            errors.put("duration", "Duration is required");
        } else if (isNullOrEmpty(metadata.getDuration()) || !Pattern.matches("^\\d{2}:\\d{2}$", metadata.getDuration())) {
            errors.put("duration", "Duration must be in mm:ss format with leading zeros");
        } else {
            String[] parts = metadata.getDuration().split(":");
            int minutes = Integer.parseInt(parts[0]);
            int seconds = Integer.parseInt(parts[1]);
            if (minutes < 0 || minutes > 59 || seconds < 0 || seconds > 59) {
                errors.put("duration", "Duration must be in mm:ss format with leading zeros");
            }
        }

        if (metadata.getYear() == null) {
            errors.put("year", "Year is required");
        } else if (isNullOrEmpty(metadata.getYear()) || !Pattern.matches("^(19|20)\\d{2}$", metadata.getYear())) {
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
