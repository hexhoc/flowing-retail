package io.flowing.retail.order.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Правила смены статусов.
 */
public final class StatusChangeRuleSet<S, E> {

    private BiConsumer<E, S> statusUpdater;

    private Supplier<RuntimeException> invalidChangeExceptionSupplier;

    private Map<S, Map<S, Consumer<E>>> rules;

    private Map<S, Consumer<E>> forceRules;

    private StatusChangeRuleSet() {
    }

    /**
     * Смена статуса.
     *
     * @param entity
     *         Сущность у которой меняется статус.
     * @param status
     *         Старый статус.
     * @param newStatus
     *         Новый статус.
     *
     * @return Сущность после всех действий по смене статуса.
     */
    public E change(E entity, S status, S newStatus) {
        if (Objects.equals(status, newStatus)) {
            return entity;
        }
        var optionalAction = Optional.ofNullable(rules.get(status))
                                .map(fromMap -> fromMap.get(newStatus));
        var action = optionalAction.orElseThrow(invalidChangeExceptionSupplier);
        action.accept(entity);
        statusUpdater.accept(entity, newStatus);
        return entity;
    }

    /**
     * Принудительная смена статуса.
     *
     * @param entity
     *         Сущность у которой меняется статус.
     * @param newStatus
     *         Новый статус.
     *
     * @return Сущность после всех действий по смене статуса.
     */
    public E forceChange(E entity, S newStatus) {
        var optionalAction = Optional.ofNullable(forceRules.get(newStatus));
        var action = optionalAction.orElseThrow(invalidChangeExceptionSupplier);
        action.accept(entity);
        statusUpdater.accept(entity, newStatus);
        return entity;
    }

    /**
     * Билдер.
     */
    public static <S, E> CoalStackStatusChangeRuleSetBuilder<S, E> builder(
            BiConsumer<E, S> statusUpdater,
            Supplier<RuntimeException> invalidChangeExceptionSupplier) {
        return new CoalStackStatusChangeRuleSetBuilder<S, E>(
                statusUpdater,
                invalidChangeExceptionSupplier);
    }

    /**
     * Билдер.
     */
    public static final class CoalStackStatusChangeRuleSetBuilder<S, E> {

        private final StatusChangeRuleSet<S, E> obj;

        private CoalStackStatusChangeRuleSetBuilder(BiConsumer<E, S> statusUpdater,
                                                    Supplier<RuntimeException> invalidChangeExceptionSupplier) {
            this.obj = new StatusChangeRuleSet<>();
            this.obj.statusUpdater = statusUpdater;
            this.obj.invalidChangeExceptionSupplier = invalidChangeExceptionSupplier;
            this.obj.rules = new HashMap<>();
            this.obj.forceRules = new HashMap<>();
        }

        /**
         * Добавление правила перевода статуса.
         *
         * @param from
         *         Из какого.
         * @param to
         *         В какой.
         * @param action
         *         Действие на данную смену статуса.
         *
         * @return Билдер.
         */
        public CoalStackStatusChangeRuleSetBuilder<S, E> rule(S from, S to, Consumer<E> action) {
            this.obj.rules.computeIfAbsent(from, status -> new HashMap<>()).put(to, action);
            return this;
        }

        /**
         * Добавление правила принудительного перевода статуса.
         *
         * @param to
         *         В какой.
         * @param action
         *         Действие на данную смену статуса.
         *
         * @return Билдер.
         */
        public CoalStackStatusChangeRuleSetBuilder<S, E> forceRule(S to, Consumer<E> action) {
            this.obj.forceRules.put(to, action);
            return this;
        }

        /**
         * Создание объекта правил смены статуса.
         *
         * @return Объект правил.
         */
        public StatusChangeRuleSet<S, E> build() {
            return obj;
        }

    }

}
