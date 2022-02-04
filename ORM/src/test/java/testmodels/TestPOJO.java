package testmodels;

public class TestPOJO {

    private int id;
    private short int16;
    private int int32;
    private long int64;
    private double float64;
    private String string;
    private char character;

    public TestPOJO(short int16, int int32, long int64, double float64, String string, char character) {
        this.id = 0;
        this.int16 = int16;
        this.int32 = int32;
        this.int64 = int64;
        this.float64 = float64;
        this.string = string;
        this.character = character;
    }

    public void setString(String string) {
        this.string = string;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TestPOJO() {
    }
}
