public class EloTelTag extends Equipo {
    protected String name;

    public EloTelTag(String owner_name, String name, float x, float y) {
        this.name = name;
        super(owner_name, x, y);
    }

    public String getName(){
        return name;
    }

    public boolean isWithinRange(Cellular cell){

        //basic math
        Double distance = Math.sqrt( Math.pow(cell.getX() - this.getX(), 2) + Math.pow(cell.getY() - this.getY(), 2) );
        if (distance <= 10) return true;
        return false;
    }

}