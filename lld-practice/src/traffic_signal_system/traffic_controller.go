package main

import (
	"context"
	"sync"
	"time"
)

type TrafficController struct {
	roads map[string]*Road
	mu    sync.Mutex
}

var instance *TrafficController
var once sync.Once

// GetTrafficController returns the singleton instance of TrafficController
func GetTrafficController() *TrafficController {
	once.Do(func() {
		instance = &TrafficController{roads: make(map[string]*Road)}
	})
	return instance
}

func (tc *TrafficController) AddRoad(road *Road) {
	tc.mu.Lock()
	defer tc.mu.Unlock()
	tc.roads[road.ID] = road
}

func (tc *TrafficController) StartTrafficControl(ctx context.Context) {
	tc.mu.Lock()
	trafficLights := make([]*TrafficLight, 0, len(tc.roads))
	for _, road := range tc.roads {
		if road.TrafficLight != nil {
			trafficLights = append(trafficLights, road.TrafficLight)
		}
	}
	tc.mu.Unlock()

	var workers sync.WaitGroup
	workers.Add(len(trafficLights))
	for _, trafficLight := range trafficLights {
		go func(tl *TrafficLight) {
			defer workers.Done()

			for {
				if !waitForSignal(ctx, time.Duration(tl.RedDuration)*time.Millisecond) {
					return
				}
				tl.ChangeSignal(Green)
				if !waitForSignal(ctx, time.Duration(tl.GreenDuration)*time.Millisecond) {
					return
				}
				tl.ChangeSignal(Yellow)
				if !waitForSignal(ctx, time.Duration(tl.YellowDuration)*time.Millisecond) {
					return
				}
				tl.ChangeSignal(Red)
			}
		}(trafficLight)
	}
	workers.Wait()
}

func waitForSignal(ctx context.Context, duration time.Duration) bool {
	timer := time.NewTimer(duration)
	defer timer.Stop()

	select {
	case <-timer.C:
		return true
	case <-ctx.Done():
		return false
	}
}

func (tc *TrafficController) HandleEmergency(roadID string) {
	tc.mu.Lock()
	defer tc.mu.Unlock()

	if road, exists := tc.roads[roadID]; exists {
		trafficLight := road.TrafficLight
		trafficLight.ChangeSignal(Green)
		// Emergency handling logic here
	}
}
