package me.deejack.webnoveltags.models.json;

import com.google.gson.annotations.SerializedName;
import me.deejack.webnoveltags.models.json.tags.JsonTag;

import java.util.Map;

public class NextData {
    @SerializedName("props")
    private Props props;

    public Props getProps() {
        return props;
    }

    public class Props {
        @SerializedName("pageProps")
        private PageProps pageProps;

        public PageProps getPageProps() {
            return pageProps;
        }

        public class PageProps {
            @SerializedName("tagsArr")
            private JsonTag[] tags;
            @SerializedName("letterCountMap")
            private Map<String, Integer> letterCount;

            public JsonTag[] getTags() {
                return tags;
            }

            public Map<String, Integer> getLetterCount() {
                return letterCount;
            }
        }
    }
}
