package main

import "fmt"

func main() {
	fmt.Println("** starting.. **")

	p1 := MyProcess{}
	p1.Start(nil, 5)

	go run(p1)

	fmt.Println("** ending.. press enter.. **")
	var i string
	fmt.Scanf("%s", &i)
}

func run(process MyProcess) {
	for {
		fmt.Println(process.Recv())
	}
}

type MyProcess struct {
	name    string
	mailbox chan interface{}
	peers   []MyProcess
}

func (p MyProcess) Send(message interface{}) {
	p.mailbox <- message
}

func (p MyProcess) Recv() interface{} {
	return <-p.mailbox
}

func (p MyProcess) Start(peers []MyProcess, boxsize int) {
	p.mailbox = make(chan interface{}, boxsize)
	p.peers = peers
}
