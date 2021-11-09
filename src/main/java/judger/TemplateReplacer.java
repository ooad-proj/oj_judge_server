package judger;

import cn.hutool.core.util.ReUtil;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TemplateReplacer {
    static String patternJava = "^([\\s\\S]*)//--//--([\\s\\S]*)//--//--([\\s\\S]*)$";
    static String patternPython = "^([\\s\\S]*)##--##--([\\s\\S]*)##--##--([\\s\\S]*)$";
    List<Template> templates;
    String code;
    String language;

    public TemplateReplacer(List<Template> templates, String code, String language) {
        this.templates = templates;
        this.code = code;
        this.language = language;
    }

    public String replace() {
        if (templates == null) return code;
        for (Template t : templates) {
            if (t.language.equals(language)) {
                return getResult(t.code, code);
            }
        }
        return code;
    }

    public String getResult(String template, String code) {

        Pattern pattern = Pattern.compile(language.equals("java") ? patternJava : patternPython, Pattern.DOTALL);
        Matcher matcher = pattern.matcher(template);
        if (matcher.find()) {
            String start = matcher.group(1);
            String end = matcher.group(3);
            return start + code + end;

        }
        return "XX";
    }

}
