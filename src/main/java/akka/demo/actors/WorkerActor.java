package akka.demo;

import java.lang.Math;
import akka.actor.AbstractActor;
import akka.actor.Status;
import akka.japi.pf.ReceiveBuilder;

public class WorkerActor extends AbstractActor {
    public WorkerActor() {
        receive(ReceiveBuilder
            .match(Work.class, s -> {
                Work work = (Work) s;
                double result = calculatePiFor(work.getStart(), work.getNumElements());
                context().sender().tell(new Result(result), self());
            })
            .matchAny( o -> sender().tell(new Status.Failure(new ClassNotFoundException()), self())).build());
    }

    private double calculatePiFor(int start, int numElements) {
        double acc = 0.0;
        for (int i = start * numElements; i <= ((start + 1) * numElements - 1); i++) {
            acc += 4.0 * (1 - (i % 2) * 2) / (2 * i + 1);
        }
        return acc;
    }
}