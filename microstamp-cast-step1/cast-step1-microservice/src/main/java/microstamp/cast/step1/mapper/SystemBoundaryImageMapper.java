package microstamp.cast.step1.mapper;

import microstamp.cast.step1.dto.systemboundaryimage.SystemBoundaryImageReadDto;
import microstamp.cast.step1.entity.SystemBoundaryImage;

public class SystemBoundaryImageMapper {

    public static SystemBoundaryImageReadDto toDto(SystemBoundaryImage image) {
        return SystemBoundaryImageReadDto.builder()
                .id(image.getId())
                .name(image.getName())
                .base64(image.getBase64())
                .build();
    }
}
