package com.koa.koalamailman.domain.reminder.application.util;

import com.koa.koalamailman.domain.reminder.domain.MandalartEmailMessage;

import java.util.Objects;

public class MailContentBuilder {

    private static String esc(String s) {
        if (s == null) return "";
        return s.replace("&","&amp;").replace("<","&lt;").replace(">","&gt;");
    }

    public static String build(MandalartEmailMessage msg) {
        String name = esc(Objects.requireNonNullElse(msg.username(), ""));
        String title = name.isBlank()
                ? "코알라 우체부에게서, 편지가 왔어요  🐨🪽✉️"
                : name + "님, 코알라 우체부가 편지를 전해요  🐨🪽✉️";

        // 공통 인라인 스타일 상수
        String font = "font-family:'Apple SD Gothic Neo','Noto Sans KR',Arial,sans-serif;";
        String containerTable = "width:600px;max-width:100%%;margin:0 auto;border-collapse:collapse;";
        String inner = "padding:20px 24px;";
        String panel = "border:1px solid #f6dcd6;background:#fff;border-radius:12px;";
        String cell  = "background:rgba(255,255,255,0.88);border:1px solid #f0d0c9;text-align:center;vertical-align:middle;"
                + "padding:14px;font-size:14px;line-height:1.45;height:120px;word-break:keep-all;";
        String core  = "background:rgba(255,111,97,0.10);border:2px solid #ff6f61;font-weight:700;"
                + "text-align:center;vertical-align:middle;padding:14px;font-size:14px;line-height:1.45;height:120px;word-break:keep-all;";
        String ctaStyle = "display:inline-block;background:#16a34a;color:#fff;text-decoration:none;padding:12px 18px;"
                + "border-radius:10px;font-weight:800;";

        return """
<!doctype html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0"/>
<meta name="x-apple-disable-message-reformatting">
<title>Koala Mailman Mandal-Art</title>
</head>
<body style="margin:0;padding:0;background:#faeee9;%s">
<table role="presentation" width="100%%" cellspacing="0" cellpadding="0" border="0" style="border-collapse:collapse;">
<tr><td align="center">
  <table role="presentation" cellspacing="0" cellpadding="0" border="0" style="%s">
    <!-- 헤더 로고 -->
     <tr>
       <td style="%s">
         <table role="presentation" width="100%%" cellspacing="0" cellpadding="0" border="0" style="border-collapse:collapse;">
           <tr>
             <td align="left" style="%s">
               <img src="%s" alt="코알라 우체부"
                    width="120"
                    style="display:block;border:0;max-width:60px;height:auto"/>
             </td>
           </tr>
         </table>
       </td>
     </tr>
     
     <!-- 히어로 -->
     <!--
     <tr>
       <td>
         <img src="%s"
              alt="Hero Image"
              width="600"
              style="display:block;border:0;width:100%%;max-width:600px;height:auto"/>
       </td>
     </tr>
    -->
    
    <!-- 타이틀 -->
    <tr>
      <td style="%s">
        <div style="%s font-size:22px;font-weight:800;margin:0 0 6px;">%s</div>
        <div style="%s font-size:14px;color:#666;margin:0;">작성했던 목표들을 잊지말아요.</div>
      </td>
    </tr>

    <!-- 만다라트 -->
    <tr>
      <td style="%s">
        <table role="presentation" width="100%%" cellspacing="0" cellpadding="0" border="0" style="border-collapse:collapse;table-layout:fixed;">
          <tr>
            <td style="%s">%s</td>
            <td style="%s">%s</td>
            <td style="%s">%s</td>
          </tr>
          <tr>
            <td style="%s">%s</td>
            <td style="%s">%s</td>
            <td style="%s">%s</td>
          </tr>
          <tr>
            <td style="%s">%s</td>
            <td style="%s">%s</td>
            <td style="%s">%s</td>
          </tr>
        </table>
      </td>
    </tr>

    <!-- 오늘의 팁 -->
    <tr>
      <td style="%s">
        <table role="presentation" width="100%%" cellspacing="0" cellpadding="0" border="0" style="%s">
          <tr>
            <td style="%s font-size:14px;">
              <strong style="color:#ff6f61;">오늘의 팁</strong> — %s
            </td>
          </tr>
        </table>
      </td>
    </tr>

    <!-- CTA -->
    <tr>
      <td style="%s;text-align:center;">
        <a href="%s" style="%s">목표 다시 보기</a>
      </td>
    </tr>

    <!-- 푸터 -->
    <tr>
      <td style="%s;text-align:center;font-size:12px;color:#999;line-height:1.6;">
        이 메일은 Koala Mailman 서비스 알림입니다.<br/>
      </td>
    </tr>
  </table>
</td></tr>
</table>
</body>
</html>
""".formatted(
                font,
                containerTable + font,
                inner + font,
                font,
                msg.logoUrl(),
                msg.heroUrl(),
                inner + font,
                font, title,
                font,
                inner + font,
                cell + font, msg.grid()[0][0],
                cell + font, msg.grid()[0][1],
                cell + font, msg.grid()[0][2],
                cell + font, msg.grid()[1][0],
                core + font, msg.grid()[1][1],
                cell + font, msg.grid()[1][2],
                cell + font, msg.grid()[2][0],
                cell + font, msg.grid()[2][1],
                cell + font, msg.grid()[2][2],
                inner + font,
                panel + font,
                "padding:16px 18px;" + font, msg.tip(),
                inner + font,
                msg.ctaUrl(), ctaStyle,
                inner + font
        );
    }
}
