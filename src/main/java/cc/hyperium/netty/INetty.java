package cc.hyperium.netty;

import cc.hyperium.utils.JsonHolder;
import java.util.List;
import java.util.UUID;

public interface INetty {
   String getSession();

   UUID getPlayerUUID();

   String getPlayerName();

   void handleChat(String var1);

   void handleCrossClientData(UUID var1, JsonHolder var2);

   void party(List<String> var1);

   void setLeader(String var1);

   void addVerboseLog(String var1);
}
