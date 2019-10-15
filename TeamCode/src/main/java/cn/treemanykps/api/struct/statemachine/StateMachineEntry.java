package cn.treemanykps.api.struct.statemachine;

import cn.treemanykps.api.struct.future.Consumer;
import cn.treemanykps.api.struct.future.Predicate;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * An entry for the state machine.
 *
 * @param <T> The type which the consumer should accept.
 * @param <P> The type which the stopper should evaluate.
 */
@AllArgsConstructor
public class StateMachineEntry<T, P> implements Predicate<P>{

    @Getter private Consumer<T> battlecry;
    @Getter private Consumer<T> deathrattle;
    private Predicate<P> tester;

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
