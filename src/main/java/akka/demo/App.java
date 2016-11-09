package akka.demo;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main(String[] args) {
    	App app = new App();
    	app.calculate(Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]));
  	}
 
  	public void calculate(final int numWorkers, final int numMessages, final int numElements) {
  		ActorSystem system = ActorSystem.create("AppSystem");
        ActorRef master = system.actorOf(Props.create(
            MasterActor.class, numWorkers, numMessages, numElements), "master");
  		master.tell(new Calculate(), null);
    }
}
