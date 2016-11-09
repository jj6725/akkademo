package akka.demo;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.Status;
import akka.routing.RoundRobinPool;
import akka.japi.pf.ReceiveBuilder;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class MasterActor extends AbstractActor {

	private final int numMessages;
  	private final int numElements;
  	private final int numWorkers;
  	private int numResults;
 	private double pi;
  	private final long start = System.currentTimeMillis();
	private final ActorRef workerActorRouter;

	public MasterActor(final int numWorkers, int numMessages, int numElements) {
		this.numMessages = numMessages;
		this.numElements = numElements;
		this.numWorkers = numWorkers;
		workerActorRouter = this.getContext()
			.actorOf(new RoundRobinPool(numWorkers)
			.props(Props.create(WorkerActor.class)),"workerActorRouter");

		receive(ReceiveBuilder
			.match(Calculate.class, s -> {
				for (int start = 0; start < numMessages; start++) {
			    	workerActorRouter.tell(new Work(start, numElements), self());
			    }
			})
			.match(Result.class, s -> {
				Result result = (Result) s;
				pi += result.getValue();
				numResults += 1;
				if (numResults == numMessages) {
					// Send the result to the listener
					System.out.println(String.format("\n\tAnswer:\t%s\n\tTime: \t%s ms\n",
						pi,(System.currentTimeMillis() - start)));
					// Stops this actor and all its supervised children
					context().stop(self());
					context().system().shutdown();
				}
			})
			.matchAny( o -> sender().tell( new Status.Failure(new ClassNotFoundException()), self())).build());
	}
}