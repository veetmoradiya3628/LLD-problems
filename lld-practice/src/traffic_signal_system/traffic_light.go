package main

import (
	"fmt"
	"sync"
)

type TrafficLight struct {
	ID             string
	CurrentSignal  Signal
	RedDuration    int
	YellowDuration int
	GreenDuration  int
	mu             sync.RWMutex
}

func NewTrafficLight(id string, redDuration, yellowDuration, greenDuration int) *TrafficLight {
	return &TrafficLight{
		ID:             id,
		RedDuration:    redDuration,
		YellowDuration: yellowDuration,
		GreenDuration:  greenDuration,
		CurrentSignal:  Red,
	}
}

func (tl *TrafficLight) ChangeSignal(newSignal Signal) {
	tl.mu.Lock()
	tl.CurrentSignal = newSignal
	tl.mu.Unlock()
	tl.notifyObservers(newSignal)
}

func (tl *TrafficLight) GetCurrentSignal() Signal {
	tl.mu.RLock()
	defer tl.mu.RUnlock()

	return tl.CurrentSignal
}

func (tl *TrafficLight) notifyObservers(signal Signal) {
	fmt.Printf("Traffic light %s changed to %s\n", tl.ID, signal)
}
