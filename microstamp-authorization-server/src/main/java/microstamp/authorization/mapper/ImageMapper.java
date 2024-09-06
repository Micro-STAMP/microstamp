package microstamp.authorization.mapper;

import microstamp.authorization.dto.ImageReadDto;
import microstamp.authorization.entity.Image;

public class ImageMapper {

    public static ImageReadDto toDto(Image image) {
        return ImageReadDto.builder()
                .id(image.getId())
                .fileName(image.getFileName())
                .base64(image.getBase64())
                .build();
    }
}
