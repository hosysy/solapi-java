package solapi.app;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.io.IOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import model.response.GroupModel;
import utilities.APIInit;

/**
 * 한번 요청으로 1만건까지 SMS 발송이 가능합니다.
 */
public class SendJsonSMS {
    public static void main(String[] args) {
        JsonObject params = new JsonObject();
        JsonArray messages = new JsonArray();

        JsonObject msg = new JsonObject();
        msg.addProperty("to", "01000010001");
        msg.addProperty("from", "029302266");
        msg.addProperty("text", "한글 45자, 영자 90자 이하 입력되면 자동으로 SMS타입의 메시지가 추가됩니다.");
        messages.add(msg);

        JsonObject msg2 = new JsonObject();
        msg2.addProperty("to", "01000010002");
        msg2.addProperty("from", "029302266");
        msg2.addProperty("text", "한글 45자, 영자 90자 이상 입력되면 자동으로 LMS타입의 문자메시자가 발송됩니다. 0123456789 ABCDEFGHIJKLMNOPQRSTUVWXYZ");
        messages.add(msg2);

        // 수신번호를 Array 형식으로 입력하여 동일한 내용으로 여러명에게 발송할 수 있습니다.
        JsonObject msg3 = new JsonObject();
        JsonArray toList = new JsonArray();
        toList.add("01000010003");
        toList.add("01000010004");
        msg3.add("to", toList);
        msg3.addProperty("from", "029302266");
        msg3.addProperty("text", "한글 45자, 영자 90자 이하 입력되면 자동으로 SMS타입의 메시지가 추가됩니다.");
        messages.add(msg3);

        // 타입을 명시할 경우 text 길이가 한글 45 혹은 영자 90자를 넘을 경우 오류가 발생합니다.
        JsonObject msg4 = new JsonObject();
        msg4.addProperty("type", "SMS");
        msg4.addProperty("to", "01000010005");
        msg4.addProperty("from", "029302266");
        msg4.addProperty("text", "SMS 타입에 한글 45자, 영자 90자 이상 입력되면 오류가 발생합니다. 0123456789 ABCDEFGHIJKLMNOPQRSTUVWXYZ");
        messages.add(msg4);

        // ... 최대 1만건까지 추가 가능

        params.add("messages", messages);

        Call<GroupModel> api = APIInit.getAPI().sendMessages(APIInit.getHeaders(), params);
        api.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<GroupModel> call, Response<GroupModel> response) {
                // 성공 시 200이 출력됩니다.
                if (response.isSuccessful()) {
                    System.out.println("statusCode : " + response.code());
                    GroupModel body = response.body();
                    System.out.println("groupId : " + body.getGroupId());
                    System.out.println("status: " + body.getStatus());
                    System.out.println("count: " + body.getCount().toString());
                } else {
                    try {
                        System.out.println(response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<GroupModel> call, Throwable throwable) {
                throwable.printStackTrace();
            }
        });
    }
}
