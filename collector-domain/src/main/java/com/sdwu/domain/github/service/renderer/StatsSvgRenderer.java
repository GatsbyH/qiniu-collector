 package com.sdwu.domain.github.service.renderer;

import com.sdwu.domain.github.model.valobj.DevelopeVo;
import com.sdwu.domain.github.model.valobj.LanguageCountRespVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class StatsSvgRenderer {

    private static final String FONT_FAMILY = "'Segoe UI', Ubuntu, Sans-Serif";
    private static final int CARD_WIDTH = 495;
    private static final int CARD_HEIGHT = 480;  // 增加高度以适应所有内容

    @Cacheable(key = "#stats.login + '-' + #languages.hashCode()")
    public String renderDeveloperStats(DevelopeVo stats, List<LanguageCountRespVo> languages) {
        StringBuilder svg = new StringBuilder();
        svg.append(getHeader());
        svg.append(getStyle());
        svg.append(getBackground());
        svg.append(renderMainStats(stats));
        svg.append(renderLanguageStats(languages));
        svg.append("</svg>");
        return svg.toString();
    }

    private String getHeader() {
        return String.format("<svg width=\"%d\" height=\"%d\" viewBox=\"0 0 %d %d\" xmlns=\"http://www.w3.org/2000/svg\">",
                CARD_WIDTH, CARD_HEIGHT, CARD_WIDTH, CARD_HEIGHT);
    }

    private String getStyle() {
        return "<style>\n" +
                "  .header { font: 600 18px " + FONT_FAMILY + "; fill: #2f80ed; }\n" +
                "  .stat { font: 600 14px " + FONT_FAMILY + "; fill: #333; }\n" +
                "  .stat-label { font: 400 12px " + FONT_FAMILY + "; fill: #666; }\n" +
                "  .score { font: 600 14px " + FONT_FAMILY + "; fill: #28a745; }\n" +
                "  .lang-name { font: 400 12px " + FONT_FAMILY + "; fill: #333; }\n" +
                "  .lang-progress { fill: #58a6ff; }\n" +
                "  .lang-bg { fill: #eee; }\n" +
                "  .avatar { border-radius: 50%; }\n" +
                "  .section-title { font: 600 16px " + FONT_FAMILY + "; fill: #2f80ed; }\n" +
                "</style>";
    }

    private String getBackground() {
        return String.format("<rect x=\"0.5\" y=\"0.5\" width=\"%d\" height=\"%d\" " +
                "rx=\"4.5\" fill=\"#fffefe\" stroke=\"#e4e2e2\"/>",
                CARD_WIDTH - 1, CARD_HEIGHT - 1);
    }

    private String renderMainStats(DevelopeVo stats) {
        StringBuilder statsHtml = new StringBuilder();

        // 头部区域（头像和标题）
       if (stats.getAvatarUrl() != null) {
        statsHtml.append(String.format(
            "<image x=\"25\" y=\"20\" width=\"40\" height=\"40\" href=\"%s\" class=\"avatar\"/>",
            stats.getAvatarUrl()
        ));
        statsHtml.append(String.format("<text x=\"75\" y=\"45\" class=\"header\">%s</text>", stats.getLogin()));
        } else {
        statsHtml.append(String.format("<text x=\"25\" y=\"45\" class=\"header\">%s</text>", stats.getLogin()));
       }
        // 定义布局参数
        int leftColX = 25;
        int rightColX = CARD_WIDTH / 2 + 25;
        int startY = 90;
        int rowHeight = 30;

        // 基础统计区域标题
        statsHtml.append(String.format(
            "<text x=\"%d\" y=\"%d\" class=\"section-title\">基础统计</text>",
            leftColX, startY - 10
        ));

        // 左列 - 基础统计
        int currentY = startY + 20;
        statsHtml.append(createStatRow("提交次数", stats.getTotalCommits(), leftColX, currentY));
        currentY += rowHeight;
        statsHtml.append(createStatRow("拉取请求", stats.getTotalPRs(), leftColX, currentY));
        currentY += rowHeight;
        statsHtml.append(createStatRow("问题数量", stats.getTotalIssues(), leftColX, currentY));
        currentY += rowHeight;
        statsHtml.append(createStatRow("代码审查", stats.getTotalReviews(), leftColX, currentY));
        currentY += rowHeight;
        statsHtml.append(createStatRow("获得星标", stats.getTotalStars(), leftColX, currentY));
        currentY += rowHeight;
        statsHtml.append(createStatRow("关注者数", stats.getTotalFollowers(), leftColX, currentY));
        currentY += rowHeight;
        statsHtml.append(createStatRow("贡献项目", stats.getContributeTo(), leftColX, currentY));

        // 得分区域标题
        statsHtml.append(String.format(
            "<text x=\"%d\" y=\"%d\" class=\"section-title\">得分详情</text>",
            rightColX, startY - 10
        ));

        // 右列 - 得分统计
        currentY = startY + 20;
        statsHtml.append(createScoreRow("总得分", stats.getTotalScore(), rightColX, currentY));
        currentY += rowHeight;
        statsHtml.append(createScoreRow("提交得分", stats.getCommitScore(), rightColX, currentY));
        currentY += rowHeight;
        statsHtml.append(createScoreRow("PR得分", stats.getPrScore(), rightColX, currentY));
        currentY += rowHeight;
        statsHtml.append(createScoreRow("问题得分", stats.getIssueScore(), rightColX, currentY));
        currentY += rowHeight;
        statsHtml.append(createScoreRow("审查得分", stats.getReviewScore(), rightColX, currentY));
        currentY += rowHeight;
        statsHtml.append(createScoreRow("星标得分", stats.getStarScore(), rightColX, currentY));
        currentY += rowHeight;
        statsHtml.append(createScoreRow("关注得分", stats.getFollowerScore(), rightColX, currentY));

        // 额外信息区域
        currentY += rowHeight;
        if (stats.getNation() != null && !stats.getNation().isEmpty()) {
            statsHtml.append(createTextRow("所属国家", stats.getNation(), leftColX, currentY));
        }

        return statsHtml.toString();
    }

    private String createStatRow(String label, Integer value, int x, int y) {
        return String.format("<g transform=\"translate(%d, %d)\">" +
                "<text class=\"stat-label\">%s:</text>" +
                "<text x=\"100\" class=\"stat\">%s</text>" +
                "</g>",
                x, y, label, value != null ? String.format("%,d", value) : "N/A");
    }

    private String createScoreRow(String label, Double value, int x, int y) {
        return String.format("<g transform=\"translate(%d, %d)\">" +
                "<text class=\"stat-label\">%s:</text>" +
                "<text x=\"100\" class=\"score\">%.2f</text>" +
                "</g>",
                x, y, label, value != null ? value : 0.0);
    }

    private String createTextRow(String label, String value, int x, int y) {
        return String.format("<g transform=\"translate(%d, %d)\">" +
                "<text class=\"stat-label\">%s:</text>" +
                "<text x=\"100\" class=\"stat\">%s</text>" +
                "</g>",
                x, y, label, value != null ? value : "N/A");
    }

    private String renderLanguageStats(List<LanguageCountRespVo> languages) {
        if (languages == null || languages.isEmpty()) {
            return "";
        }

        StringBuilder langStats = new StringBuilder();
        int startY = 330;  // 调整语言统计的起始位置

        langStats.append(String.format(
            "<text x=\"25\" y=\"%d\" class=\"section-title\">常用编程语言</text>",
            startY
        ));

        int total = languages.stream()
                .mapToInt(LanguageCountRespVo::getA)
                .sum();

        List<LanguageCountRespVo> topLanguages = languages.stream()
                .sorted(Comparator.comparing(LanguageCountRespVo::getA).reversed())
                .limit(5)
                .collect(Collectors.toList());

        int yOffset = startY + 20;
        int barWidth = CARD_WIDTH - 60;

        for (LanguageCountRespVo lang : topLanguages) {
            double percentage = (lang.getA() * 100.0) / total;
            int width = (int) (barWidth * percentage / 100);
            String color = getLanguageColor(lang.getItem());

            langStats.append(String.format("<g transform=\"translate(25, %d)\">" +
                    "<rect class=\"lang-bg\" x=\"0\" y=\"0\" width=\"%d\" height=\"8\" rx=\"5\"/>" +
                    "<rect style=\"fill: %s\" x=\"0\" y=\"0\" width=\"%d\" height=\"8\" rx=\"5\"/>" +
                    "<text x=\"0\" y=\"20\" class=\"lang-name\">%s (%.1f%%)</text>" +
                    "</g>",
                    yOffset, barWidth, color, width, lang.getItem(), percentage));

            yOffset += 25;
        }

        return langStats.toString();
    }

   private String getLanguageColor(String language) {
    if (language == null) {
        return "#858585";
    }

    // 将语言名称转换为小写以进行大小写不敏感的匹配
    String lang = language.toLowerCase();

    switch (lang) {
        // 主流语言
        case "javascript":
            return "#f1e05a";
        case "python":
            return "#3572A5";
        case "java":
            return "#b07219";
        case "typescript":
            return "#2b7489";
        case "c++":
        case "cpp":
            return "#f34b7d";
        case "c":
            return "#555555";
        case "ruby":
            return "#701516";
        case "go":
            return "#00ADD8";
        case "rust":
            return "#dea584";

        // Web 相关
        case "html":
            return "#e34c26";
        case "css":
            return "#563d7c";
        case "vue":
            return "#41b883";
        case "react":
            return "#61dafb";
        case "php":
            return "#4F5D95";

        // 移动开发
        case "swift":
            return "#ffac45";
        case "kotlin":
            return "#F18E33";
        case "dart":
            return "#00B4AB";
        case "objective-c":
            return "#438eff";

        // 数据科学
        case "r":
            return "#198CE7";
        case "julia":
            return "#a270ba";
        case "matlab":
            return "#e16737";

        // 系统编程
        case "assembly":
            return "#6E4C13";
        case "c#":
        case "csharp":
            return "#178600";
        case "scala":
            return "#c22d40";
        case "perl":
            return "#0298c3";

        // 其他语言
        case "lua":
            return "#000080";
        case "shell":
            return "#89e051";
        case "powershell":
            return "#012456";
        case "haskell":
            return "#5e5086";
        case "elixir":
            return "#6e4a7e";
        case "groovy":
            return "#4298b8";
        case "sql":
            return "#e38c00";
        case "dockerfile":
            return "#384d54";
        case "cmake":
            return "#DA3434";
        case "yaml":
        case "yml":
            return "#cb171e";
        case "vue.js":
            return "#41b883";
        case "react.js":
            return "#61dafb";
        case "node.js":
            return "#68a063";
        case "markdown":
            return "#083fa1";
        case "json":
            return "#292929";
        case "xml":
            return "#0060ac";

        default:
            // 为未知语言生成一个基于语言名称的唯一颜色
            return generateColorForUnknownLanguage(lang);
    }
}

    /**
     * 为未知语言生成一个唯一的颜色
     * 使用语言名称的哈希值来生成颜色，确保同一语言总是得到相同的颜色
     */
    private String generateColorForUnknownLanguage(String language) {
        if (language == null || language.isEmpty()) {
            return "#858585";
        }

        // 使用语言名称的哈希值来生成颜色
        int hash = language.hashCode();

        // 确保生成的颜色不会太浅或太深
        int r = Math.abs(hash) % 156 + 100;  // 100-255
        int g = Math.abs(hash >> 8) % 156 + 100;  // 100-255
        int b = Math.abs(hash >> 16) % 156 + 100;  // 100-255

        // 转换为16进制颜色代码
        return String.format("#%02x%02x%02x", r, g, b);
    }

    /**
     * 获取语言的显示名称
     * 用于统一语言的显示方式
     */
    private String getLanguageDisplayName(String language) {
        if (language == null) {
            return "Unknown";
        }

        // 特殊处理一些语言的显示名称
        switch (language.toLowerCase()) {
            case "cpp":
                return "C++";
            case "csharp":
                return "C#";
            case "javascript":
                return "JavaScript";
            case "typescript":
                return "TypeScript";
            case "vue.js":
                return "Vue";
            case "react.js":
                return "React";
            case "node.js":
                return "Node.js";
            default:
                // 首字母大写
                return language.substring(0, 1).toUpperCase() + language.substring(1);
        }
    }
}
