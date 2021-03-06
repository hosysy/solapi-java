package solapi.app;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.io.IOException;
import model.request.Message;
import model.request.KakaoOptions;
import model.request.KakaoButtons;
import model.request.KakaoButtonItem;
import model.response.MessageModel;
import utilities.APIInit;

public class SendAlimtalkOneButton {
    public static void main(String[] args) {

        // 순서 및 내용을 등록된 템플릿 버튼 구성 그대로 입력해야 합니다.
        // 변수는 치환된 값으로 입력해 줍니다. 예) https://${url} ---> https://example.com
        KakaoButtons buttons = new KakaoButtons();
        buttons.add(new KakaoButtonItem("[버튼이름]", "[버튼 종류(AL: 앱링크, WL: 웹링크, DS: 배송조회, BK: 키워드, MD: 전달)]", "[모바일 링크]", "[PC 링크]", "[안드로이드 앱링크]", "[IOS 앱링크]"));
        // buttons.add(new KakaoButtonItem("웹링크", "WL", "https://m.example.com", "https://example.com", null, null));
        // buttons.add(new KakaoButtonItem("앱실행", "AL", null, null, "android_scheme://", "ios_scheme://"));
        // buttons.add(new KakaoButtonItem("배송조회", "DS", null, null, null, null)); // 알림톡에서만 DS 타입 발송 가능
        // buttons.add(new KakaoButtonItem("봇키워드", "BK", null, null, null, null)); // 봇키워드를 챗봇에 전달
        // buttons.add(new KakaoButtonItem("메시지 전달", "MD", null, null, null, null)); // 상담원에게 메시지 전달

        // 알림톡은 반드시 pfId와 templateId값을 입력해야 합니다.
        KakaoOptions kakaoOptions = new KakaoOptions("[pfID를 입력하세요]", "[템플릿ID를 입력하세요]", true, null, buttons);

        // 전송할 메시지는 변수를 치환하여 입력해 줍니다.  예) #{이름}님 가입을 환영합니다.  ---> 홍길동님 가입을 환영합니다.
        // 변수 이외의 내용은 100% 일치해야 하며, 단 줄내림은 마음껏 하실 수 있습니다. 예) #{이름}님 가입을 환영합니다. ----> 홍길동님\n\n가입을\n\n환영합니다.
        Message message = new Message("[수신번호를 입력하세요]", "[발신번호를 입력하세요]", "[전송할 메시지를 입력하세요]", kakaoOptions);

        Call<MessageModel> api = APIInit.getAPI().sendMessage(APIInit.getHeaders(), message);
        api.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                // 성공 시 200이 출력됩니다.
                if (response.isSuccessful()) {
                    System.out.println("statusCode : " + response.code());
                    MessageModel body = response.body();
                    System.out.println("groupId : " + body.getGroupId());
                    System.out.println("messageId : " + body.getMessageId());
                    System.out.println("to : " + body.getTo());
                    System.out.println("from : " + body.getFrom());
                    System.out.println("type : " + body.getType());
                    System.out.println("statusCode : " + body.getStatusCode());
                    System.out.println("statusMessage : " + body.getStatusMessage());
                    System.out.println("customFields : " + body.getCustomFields());
                } else {
                    try {
                        System.out.println(response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<MessageModel> call, Throwable throwable) {
                throwable.printStackTrace();
            }
        });
    }
}
