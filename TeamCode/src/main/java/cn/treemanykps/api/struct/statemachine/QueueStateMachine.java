package cn.treemanykps.api.struct.statemachine;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import cn.treemanykps.api.struct.future.Consumer;
import cn.treemanykps.api.struct.future.Predicate;

/**
 * A state machine that abuse the usage of generics.
 *
 * <p>
 * Each state entry should contain 2 consumers; one to trigger when the state starts and
 * one to trigger when the state ends.
 * </p>
 *
 * @param <T> The type which the consumer should accept.
 * @param <P> The type which the stopper should check. Pass in a type of this per update.
 */
public class QueueStateMachine<T, P> {

    private Queue<StateMachineEntry<T, P>> queue;

    public QueueStateMachine() {
        this.queue = new ConcurrentLinkedQueue<>();
        this.addState(StateMachineEntry.<T, P>getDummy());
    }

    /**
     * Adds a state to the state machine.
     *
     * @param starter The consumer to call when the state starts.
     * @param ender The consumer to call when the state ends.
     * @param tester The stopper. Should return true when the state is supposed to end.
     */
    public void addState(Consumer<T> starter, Consumer<T> ender, Predicate<P> tester) {
        this.queue.add(new StateMachineEntry<>(starter, ender, tester));
    }

    public void addState(StateMachineEntry<T, P> state) {
        this.queue.add(state);
    }
}
