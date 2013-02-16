package no.mobmad;

public enum LogColor
{
    RED("\u001B[31m", "RED"),
    GREEN("\u001B[32m", "GREEN"),
    YELLOW("\u001B[33m", "YELLOW"),
    BLUE("\u001B[34m", "BLUE"),
    PURPLE("\u001B[35m", "PURPLE"),
    CYAN("\u001B[36m", "CYAN"),
    WHITE("\u001B[37m", "WHITE");

    private String asciiColor;
    private String colorName;

    LogColor(String asciiColor, String colorName)
    {

        this.asciiColor = asciiColor;
        this.colorName = colorName;
    }

    public String getAsciiColor()
    {
        return asciiColor;
    }

    public String getColorName()
    {
        return colorName;
    }
}
