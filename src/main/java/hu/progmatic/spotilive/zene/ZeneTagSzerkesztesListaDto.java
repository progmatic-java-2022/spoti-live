package hu.progmatic.spotilive.zene;

import hu.progmatic.spotilive.tag.Tag;
import hu.progmatic.spotilive.tag.TagDto;
import hu.progmatic.spotilive.tag.TagKategoria;
import hu.progmatic.spotilive.tag.TagService;
import lombok.Builder;
import lombok.Value;
import org.hibernate.annotations.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Value
@Builder
public class ZeneTagSzerkesztesListaDto {

    Integer zeneId;
    Map<TagKategoria, TagekByKategoriak> tagByKategoria;

    public static ZeneTagSzerkesztesListaDto factory(Zene zene, List<Tag> tagek) {
        return ZeneTagSzerkesztesListaDto
                .builder()
                .zeneId(zene.getId())
                .tagByKategoria(getKategoriak(zene, tagek))
                .build();

    }

    public static Map<TagKategoria, TagekByKategoriak> getKategoriak(Zene zene, List<Tag> tagek) {
        Map<TagKategoria, TagekByKategoriak> kategoriak = new HashMap<>();
        for (TagKategoria kategoria : List.of(TagKategoria.values())) {
            kategoriak.put(kategoria, new TagekByKategoriak(getHozzaadott(zene, kategoria), getNemhozzaadott(zene, tagek, kategoria)));
        }
        return kategoriak;
    }

    private static List<TagDto> getNemhozzaadott(Zene zene, List<Tag> tagek, TagKategoria kategoria) {
        List<TagDto> output = new java.util.ArrayList<>(tagek.stream().map(TagDto::factory).toList());
        for (Tag tag : tagek) {
            if (zene.getTagToZeneEntityList().stream().map(TagToZene::getTag).toList().contains(tag) || !tag.getTagKategoria().equals(kategoria)) {
                output.remove(TagDto.factory(tag));
            }
        }
        return output;
    }

    private static List<TagDto> getHozzaadott(Zene zene, TagKategoria kategoria) {
        return zene.getTagToZeneEntityList()
                .stream()
                .map(TagToZene::getTag)
                .filter(tag -> tag.getTagKategoria().equals(kategoria))
                .map(TagDto::factory)
                .toList();
    }

    @Value

    public static class TagekByKategoriak {
        List<TagDto> hozzaadott;
        List<TagDto> nemHozzaadott;

        public TagekByKategoriak(List<TagDto> hozzaadott, List<TagDto> nemHozzaadott) {
            this.hozzaadott = hozzaadott;
            this.nemHozzaadott = nemHozzaadott;
        }
    }

}
