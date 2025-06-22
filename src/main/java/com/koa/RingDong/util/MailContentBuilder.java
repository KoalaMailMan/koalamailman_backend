package com.koa.RingDong.util;

import com.koa.RingDong.entity.Cell;
import com.koa.RingDong.entity.MainBlock;
import com.koa.RingDong.entity.SubBlock;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class MailContentBuilder {

    private static final Map<Integer, String> pastelColors = new HashMap<>();
    static {
        pastelColors.put(0, "#eff6ff");  // blue-50
        pastelColors.put(1, "#f0fdfa");  // teal-50
        pastelColors.put(2, "#ecfeff");  // cyan-50
        pastelColors.put(3, "#fffbeb");  // amber-50
        pastelColors.put(5, "#fff1f2");  // rose-50
        pastelColors.put(6, "#f5f3ff");  // violet-50
        pastelColors.put(7, "#ecfdf5");  // emerald-50
        pastelColors.put(8, "#fff7ed");  // orange-50
        pastelColors.put(4, "#fef3c7");  // 중앙 메인블럭 yellow-100
    }

    public String buildTitle() {
        return "RingDong! 당신의 목표 잊지 않으셨나요?";
    }

    public String buildFullHtml(MainBlock mainBlock) {
        StringBuilder html = new StringBuilder();

        html.append("<div style='font-family:Arial,sans-serif; font-size:14px; color:#333;'>");

        html.append("""
<style>
  @media screen and (max-width: 600px) {
    .full-grid-table td {
      display: none;
    }
    .full-grid-table td[data-position="4"] {
      display: table-cell !important;
    }
  }
</style>
""");

        html.append(buildHeader());
        html.append("<br/>");
        html.append(buildMainOnlyHtml(mainBlock));
        html.append("<br/>");
        html.append(buildFooter());

        html.append("</div>");

        return html.toString();
    }

    private String buildHeader() {
        return """
        <div style='text-align:center; font-size:18px; font-weight:bold; color:#2c3e50; padding:12px 0;'>
            🔔 RingDong! 띵동~<br/>
            오늘도 성장하는 당신을 위해<br/>
            <span style='color:#f59e0b;'>만다라트 목표</span>를 다시 확인해보세요 💡
        </div>
    """;
    }

    private String buildFooter() {
        return "<div style='margin-top:12px; text-align:center; font-size:12px; color:#666;'>"
                + "링동과 함께 새해목표 더 이상 까먹지 말아요.<br/>"
                + "<a href='https://ringdong.kr' style='color:#007BFF; text-decoration:none;'>https://ringdong.kr</a>"
                + "</div>";
    }

    public String buildFullGridHtml(MainBlock mainBlock) {
        Map<Integer, SubBlock> subMap = mainBlock.getSubBlocks().stream()
                .collect(Collectors.toMap(SubBlock::getPosition, sb -> sb));

        StringBuilder html = new StringBuilder();
        html.append("<table class='full-grid-table' style='width:100%; border-collapse:collapse; table-layout:fixed;'>");

        for (int mainRow = 0; mainRow < 3; mainRow++) {
            html.append("<tr>");
            for (int mainCol = 0; mainCol < 3; mainCol++) {
                int position = mainRow * 3 + mainCol;
                String bgColor = pastelColors.getOrDefault(position, "#ffffff");

                html.append("<td data-position='").append(position)
                        .append("' style='border:1px solid #ccc; padding:4px; background-color:")
                        .append(bgColor)
                        .append("; vertical-align:top;'>");

                html.append("<table style='width:100%; aspect-ratio:1/1; table-layout:fixed; border-collapse:collapse;'>");

                for (int detailRow = 0; detailRow < 3; detailRow++) {
                    html.append("<tr>");
                    for (int detailCol = 0; detailCol < 3; detailCol++) {
                        int subCellPos = detailRow * 3 + detailCol;
                        boolean isCenter = (detailRow == 1 && detailCol == 1);

                        html.append("<td style='border:1px solid #ddd; font-size:10px; padding:4px; text-align:center; vertical-align:middle; width:33.3%; height:33.3%;'>");

                        html.append("<a href='https://ringdong.kr' style='color:inherit; text-decoration:none;'>");

                        if (position == 4 && isCenter) {
                            html.append("<b>").append(escape(mainBlock.getContent())).append("</b>");
                        } else if (position == 4) {
                            int subPosition = mapCenterSurroundingCellToSubPosition(detailRow, detailCol);
                            SubBlock sub = subMap.get(subPosition);
                            if (sub != null) {
                                html.append(escape(sub.getContent()));
                            }
                        } else if (isCenter) {
                            SubBlock sub = subMap.get(position);
                            if (sub != null) {
                                html.append("<b>").append(escape(sub.getContent())).append("</b>");
                            }
                        } else {
                            SubBlock sub = subMap.get(position);
                            if (sub != null) {
                                Cell cell = sub.getCells().stream()
                                        .filter(c -> c.getPosition() == subCellPos)
                                        .findFirst()
                                        .orElse(null);
                                if (cell != null) {
                                    html.append(escape(cell.getContent()));
                                }
                            }
                        }

                        html.append("</a></td>");
                    }
                    html.append("</tr>");
                }

                html.append("</table>");
                html.append("</td>");
            }
            html.append("</tr>");
        }

        html.append("</table>");
        return html.toString();
    }

    public String buildMainOnlyHtml(MainBlock mainBlock) {
        StringBuilder html = new StringBuilder();

        html.append("<table class='main-only-table' style='width:100%; border-collapse:collapse; table-layout:fixed;'>");
        html.append("<tr><td style='background-color:")
                .append(pastelColors.getOrDefault(4, "#fef3c7"))
                .append("; padding:4px; border:1px solid #ccc;'>");

        html.append("<table style='width:100%; aspect-ratio:1/1; table-layout:fixed; border-collapse:collapse;'>");

        for (int row = 0; row < 3; row++) {
            html.append("<tr>");
            for (int col = 0; col < 3; col++) {
                boolean isCenter = (row == 1 && col == 1);
                int pos = row * 3 + col;
                html.append("<td style='border:1px solid #ddd; font-size:12px; padding:4px; text-align:center; vertical-align:middle; width:33.3%; height:33.3%;'>");
                html.append("<a href='https://ringdong.kr' style='color:inherit; text-decoration:none;'>");

                if (isCenter) {
                    html.append("<b>").append(escape(mainBlock.getContent())).append("</b>");
                } else {
                    int subPosition = mapCenterSurroundingCellToSubPosition(row, col);
                    SubBlock sub = mainBlock.getSubBlocks().stream()
                            .filter(sb -> sb.getPosition() == subPosition)
                            .findFirst()
                            .orElse(null);
                    if (sub != null) {
                        html.append(escape(sub.getContent()));
                    }
                }
                html.append("</a></td>");
            }
            html.append("</tr>");
        }

        html.append("</table>");
        html.append("</td></tr></table>");

        return html.toString();
    }

    private String escape(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;");
    }

    private int mapCenterSurroundingCellToSubPosition(int row, int col) {
        if (row == 0 && col == 0) return 0;
        if (row == 0 && col == 1) return 1;
        if (row == 0 && col == 2) return 2;
        if (row == 1 && col == 0) return 3;
        if (row == 1 && col == 2) return 5;
        if (row == 2 && col == 0) return 6;
        if (row == 2 && col == 1) return 7;
        if (row == 2 && col == 2) return 8;
        return -1;
    }
}
