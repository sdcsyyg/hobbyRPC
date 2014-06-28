package business;

/**
 * Remote Business support(handler)
 * @author sdcsyyg
 *
 */
public class BusinessHandler implements Business {

    @Override
    public String doThis(String instruction) {
        System.out.println("I am 'doThis', was called just now in the 'server-end', right?");
        return "I am 'doThis', have finished your instruction : " + instruction;
    }

    @Override
    public String doThat(String instruction) {
        System.out.println("I am 'doThat', was called just now in the 'server-end', right?");
        return "I am 'doThat', have finished your instruction : " + instruction;
    }

}
