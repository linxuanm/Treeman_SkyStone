package cn.treemanykps.api.struct.statemachine;

import cn.treemanykps.api.struct.future.Consumer;
import cn.treemanykps.api.struct.future.Predicate;

/**
 * An entry for the state machine.
 *
 * @param <T> The type which the consumer should accept.
 * @param <P> The type which the stopper should evaluate.
 */
public class StateMachineEntry<T, P> implements Predicate<P> {

    private Consumer<T> battlecry;
    private Consumer<T> deathrattle;
    private Predicate<P> tester;

    public StateMachineEntry(Consumer<T> start, Consumer<T> end, Predicate<P> test) {
        this.battlecry = start;
        this.deathrattle = end;
        this.tester = test;
    }

    public static <A, B> StateMachineEntry<A, B> getDummy() {
        return new StateMachineEntry<>(
                new Consumer<A>() {
                    @Override
                    public void accept(A object) {
                    }
                },
                new Consumer<A>() {
                    @Override
                    public void accept(A object) {
                    }
                },
                new Predicate<B>() {
                    @Override
                    public boolean test(B object) {
                        return true;
                    }
                }
        );
    }

    @Override
    public boolean test(P object) {
        return this.tester.test(object);
    }
}
