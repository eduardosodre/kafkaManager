package br.com.kafkamanager.application;

public abstract class UnitUseCase<IN> {

    public abstract void execute(IN anIn);
}
