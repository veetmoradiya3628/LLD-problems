package main

import "context"

type PaymentProcessor interface {
	ProcessPayment(ctx context.Context, booking *Booking) error
}

type MockPaymentProcessor struct{}

func (MockPaymentProcessor) ProcessPayment(ctx context.Context, _ *Booking) error {
	return ctx.Err()
}
