package com.koa.koalamailman.domain.reminder.util;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

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

//    public String buildFullHtml(CoreGoal coreGoal) {
//        StringBuilder html = new StringBuilder();
//
//        html.append("<div style='font-family:Arial,sans-serif; font-size:14px; color:#333;'>");
//
//        html.append("""
//<style>
//  @media screen and (max-width: 600px) {
//    .full-grid-table td {
//      display: none;
//    }
//    .full-grid-table td[data-position="4"] {
//      display: table-cell !important;
//    }
//  }
//</style>
//""");
//
//        html.append(buildHeader());
//        html.append("<br/>");
//        html.append(buildMainHtml(coreGoal));
//        html.append("<br/>");
//        html.append(buildFooter());
//
//        html.append("</div>");
//
//        return html.toString();
//    }
//
//    private String buildHeader() {
//        return """
//        <div style='text-align:center; font-size:18px; font-weight:bold; color:#2c3e50; padding:12px 0;'>
//            🔔 RingDong! 띵동~<br/>
//            오늘도 성장하는 당신을 위해<br/>
//            <span style='color:#f59e0b;'>만다라트 목표</span>를 다시 확인해보세요 💡
//        </div>
//    """;
//    }
//
//    private String buildFooter() {
//        return "<div style='margin-top:12px; text-align:center; font-size:12px; color:#666;'>"
//                + "링동과 함께 새해목표 더 이상 까먹지 말아요.<br/>"
//                + "<a href='https://ringdong.kr' style='color:#007BFF; text-decoration:none;'>https://ringdong.kr</a>"
//                + "</div>";
//    }
//    public String buildMainHtml(CoreGoal coreGoal) {
//        StringBuilder html = new StringBuilder();
//
//        html.append("<div style='max-width:620px; margin:0 auto; padding:32px 16px; font-family:Noto Sans KR,Apple SD Gothic Neo,sans-serif;'>");
//
//        html.append("<div style='display:grid; grid-template-columns:repeat(3,1fr); gap:4px; border-radius:8px; overflow:hidden;'>");
//
//        for (int row = 0; row < 3; row++) {
//            for (int col = 0; col < 3; col++) {
//                boolean isCenter = (row == 1 && col == 1);
//                int pos = row * 3 + col;
//
//                // 색상 매핑
//                String backgroundColor;
//                if (isCenter) {
//                    backgroundColor = "#fef3c7"; // 중앙 셀 색상
//                } else {
//                    int[] pastelOrder = {0, 1, 2, 3, 5, 6, 7, 8}; // sub position
//                    int subPosition = mapCenterSurroundingCellToSubPosition(row, col);
//                    int colorIndex = subPosition >= 0 && subPosition < pastelOrder.length ? pastelOrder[subPosition] : 0;
//                    backgroundColor = switch (colorIndex) {
//                        case 0 -> "#eff6ff";
//                        case 1 -> "#f0fdfa";
//                        case 2 -> "#ecfeff";
//                        case 3 -> "#fffbeb";
//                        case 4 -> "#fef3c7"; // not used here
//                        case 5 -> "#fff1f2";
//                        case 6 -> "#f5f3ff";
//                        case 7 -> "#ecfdf5";
//                        case 8 -> "#fff7ed";
//                        default -> "#f0f0f0";
//                    };
//                }
//
//                html.append("<div style='background-color:")
//                        .append(backgroundColor)
//                        .append("; height:72px; display:flex; align-items:center; justify-content:center; font-size:14px; padding:8px; word-break:break-word;'>");
//
//                html.append("<a href='https://ringdong.kr' style='color:inherit; text-decoration:none;'>");
//
//                if (isCenter) {
//                    html.append("<b>").append(escape(coreGoal.getContent())).append("</b>");
//                } else {
//                    int subPosition = mapCenterSurroundingCellToSubPosition(row, col);
//                    MainGoal main = coreGoal.getMainGoals().stream()
//                            .filter(mg -> mg.getPosition() == subPosition)
//                            .findFirst()
//                            .orElse(null);
//                    if (main != null) {
//                        html.append(escape(main.getContent()));
//                    }
//                }
//
//                html.append("</a></div>");
//            }
//        }
//
//        html.append("</div>"); // grid
//        html.append("</div>"); // container
//
//        return html.toString();
//    }
//
//
//    private String escape(String text) {
//        if (text == null) return "";
//        return text.replace("&", "&amp;")
//                .replace("<", "&lt;")
//                .replace(">", "&gt;")
//                .replace("\"", "&quot;");
//    }
//
//    private int mapCenterSurroundingCellToSubPosition(int row, int col) {
//        if (row == 0 && col == 0) return 0;
//        if (row == 0 && col == 1) return 1;
//        if (row == 0 && col == 2) return 2;
//        if (row == 1 && col == 0) return 3;
//        if (row == 1 && col == 2) return 5;
//        if (row == 2 && col == 0) return 6;
//        if (row == 2 && col == 1) return 7;
//        if (row == 2 && col == 2) return 8;
//        return -1;
//    }
}
