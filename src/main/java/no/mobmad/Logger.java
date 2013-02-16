package no.mobmad;

import java.util.HashMap;

public class Logger
{

    public static final String ASCII_RESET = "\u001B[0m";

    private static HashMap<String, LogColor> sessionColors = new HashMap<String, LogColor>();
    private static Integer colorIndex = 0;

    private String requestID; // only unique pr. session
    private String sessionID;
    private String context;
    private LogColor color;

    public Logger(String sessionID, String requestID)
    {
        this.sessionID = sessionID;
        this.requestID = requestID;

        synchronized (colorIndex)
        {
            LogColor color = sessionColors.get(sessionID);
            LogColor[] colors = LogColor.values();
            if (color == null)
            {
                if (colorIndex == colors.length)
                {
                    colorIndex = 0;
                }
                color = colors[colorIndex++];
                sessionColors.put(sessionID, color);
            }
            this.color = color;
        }
    }

    public Logger setContext(String context)
    {
        this.context = context;
        return this;
    }

    public Logger log(LogStatus status)
    {

        int indent = (status.ordinal() * 20) + 1;

        String statusForRequest = String.format("%s (%s)",
                status.getText(),
                getShortRequestID());

        System.out.printf("%s%-30s %-15s %-" + indent + "s %s%s\n",
                color.getAsciiColor(),
                sessionID,
                context,
                "",
                statusForRequest,
                ASCII_RESET);

        return this;
    }

    private String getShortRequestID()
    {
        return requestID.substring(Math.max(0, requestID.length() - 3));
    }

    public LogColor getColor()
    {
        return color;
    }
}
