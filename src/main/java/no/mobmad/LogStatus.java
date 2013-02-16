package no.mobmad;

public enum LogStatus
{
    QUEUED("QUEUED"),
    PROCESSING("PROCESSING"),
    DONE("DONE");

    private final String text;

    LogStatus(String text)
    {
        this.text = text;
    }

    public String getText()
    {
        return text;
    }
}
