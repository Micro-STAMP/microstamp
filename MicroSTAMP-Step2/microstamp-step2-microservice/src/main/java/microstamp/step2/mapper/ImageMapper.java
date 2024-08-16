package microstamp.step2.mapper;

import microstamp.step2.dto.image.ImageReadDto;
import microstamp.step2.entity.Image;

public class ImageMapper {

    public static ImageReadDto toDto(Image image){
        return ImageReadDto.builder()
                .id(image.getId())
                .name(image.getName())
                .base64(image.getBase64())
                .build();
    }
}
