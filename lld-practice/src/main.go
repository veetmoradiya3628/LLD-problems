package main

// Define a Shape interface with Area(), Perimeter(), and Name().
// Implement the shared description formatting once in describeShape.
//
// Implement Circle and Rectangle as Shape types.
// ShapeCalculator is provided. Do not modify it.

type ShapeCalculator struct {
	shapes []Shape
}

func Constructor() ShapeCalculator {
	return ShapeCalculator{shapes: []Shape{}}
}

func (calculator *ShapeCalculator) AddCircle(radius float64) int {
	calculator.shapes = append(calculator.shapes, Circle{radius: radius})
	return len(calculator.shapes) - 1
}

func (calculator *ShapeCalculator) AddRectangle(width float64, height float64) int {
	calculator.shapes = append(calculator.shapes, Rectangle{width: width, height: height})
	return len(calculator.shapes) - 1
}

func (calculator *ShapeCalculator) Area(index int) float64 {
	return calculator.shapes[index].Area()
}

func (calculator *ShapeCalculator) Perimeter(index int) float64 {
	return calculator.shapes[index].Perimeter()
}

func (calculator *ShapeCalculator) Describe(index int) string {
	return describeShape(calculator.shapes[index])
}

func (calculator *ShapeCalculator) TotalArea() float64 {
	sum := 0.0
	for _, shape := range calculator.shapes {
		sum += shape.Area()
	}
	return sum
}

/**
 * Your ShapeCalculator object will be instantiated and called as such:
 * obj := Constructor()
 * param_1 := obj.AddCircle(radius)
 * param_2 := obj.AddRectangle(width, height)
 * param_3 := obj.Area(index)
 * param_4 := obj.Perimeter(index)
 * param_5 := obj.Describe(index)
 * param_6 := obj.TotalArea()
 */

/**
 * Your TemperatureSensor object will be instantiated and called as such:
 * obj := Constructor()
 * param_1 := obj.AddReading(value)
 * param_2 := obj.GetReadingCount()
 * param_3 := obj.GetAverage()
 * param_4 := obj.GetReadings()
 */

func main() {

}
