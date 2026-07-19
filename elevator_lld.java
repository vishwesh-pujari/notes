// ═══════════════════════════════════════════════════════════════════════════
// REQUIREMENTS
//
// Example (Tic Tac Toe):
//   1. Two players alternate placing X and O on a 3x3 grid.
//   2. A player wins by completing a row, column, or diagonal.
//   Out of Scope: UI, AI opponent, networking
// ═══════════════════════════════════════════════════════════════════════════

Requirements:

Elevator System:
1. The system should have 3 elevators and 10 floors numbered from 0 to 9.
2. Hall calls will be Up and Down. User will select the floor they want to go to inside the elevator.
3. System should make the selection of elevator for a request in an optimal way.
4. System should keep track of discrete time steps and in one unit of time, elevators should move 1 floor in the respective direction.

Error handling:
1. Invalid user request if the user presses a down key at floor 0 or up key at floor 9

Out of scope: Door mechanics.


// ═══════════════════════════════════════════════════════════════════════════
// ENTITIES & RELATIONSHIPS
//
// Example (Tic Tac Toe):
//   Game, Board, Player
// ═══════════════════════════════════════════════════════════════════════════

Entities :
1. Elevator
2. ElevatorManager
3. Request 
4. RequestType -> (Hall call / inside the elevator) enum
5. Direction -> enum (UP, DOWN, IDLE)


// ═══════════════════════════════════════════════════════════════════════════
// CLASS DESIGN
//
// Example (Tic Tac Toe):
//   class Game:
//     - board: Board
//     - currentPlayer: Player
//     + makeMove(row, col) -> bool
// ═══════════════════════════════════════════════════════════════════════════

class Elevator {
    private int id;
    private int currentFloor;
    private Direction direction;
    private List<Integer> serviceFloors; // floors on which Elevator is supposed to serve

    ----------------
    public Elevator();
    public void step();
    public int addFloor(int floor); // can come from person pressing button inside elevator or from the system providing a hall call request.
    private void openDoor(); 
}

enum Direction {
    UP, DOWN, IDLE;
}

class Request {
    int floor;
    Directon direction; // for hall calls it will pass either UP or DOWN
    RequestType requestType;

    ----------
    public Request(int floor, RequestType requestType);
}

enum RequestType {
    HALL_CALL, INSIDE_ELEVATOR;
}

class ElevatorManager {
    private List<Elevator> elevators;
    
    ---------------
    public void processRequest(Request request);
    public void step(); // calls the step of all the elevators
    private void selectBestElevator(Request request);
}


// ═══════════════════════════════════════════════════════════════════════════
// IMPLEMENTATION
// ═══════════════════════════════════════════════════════════════════════════

class Elevator {
    private int id;
    private int currentFloor;
    private Direction direction;
    private List<Integer> serviceFloors; // floors on which Elevator is supposed to serve
    private static int FLOOR_MAX = 9, FLOOR_MIN = 0;

    ----------------
    public Elevator();

    public void step() {
        if (serviceFloors.isEmpty()) {
            this.direction = IDLE;
            return;
        }

        if (this.serviceFloors.contains(currentFloor)) {
            this.serviceFloors.remove(currentFloor);
            this.openDoor();
            return; // assuming that door automatically closes and has necessary sensors
        }

        int floorsAbove = 0, floorsBelow = 0;
        for (int floor: serviceFloors) {
            if (floor < currentFloor)
                floorsBelow++;
            else
                floorsAbove++;
        }

        if (floorsBelow == 0)
            this.direction = UP;
        else if (floorsAbove == 0)
            this.direction = DOWN;
        else if (direction == IDLE) {
            if (floorsAbove > floorsBelow)
                this.direction = UP;
            else
                this.direction = DOWN;
        }

        if (direction == UP)
            currentFloor++;
        else if (direction == DOWN)
            currentFloor--;
    }


    public int addFloor(int floor) { // can come from person pressing button inside elevator or from the system providing a hall call request.
        if (floor < FLOOR_MIN || floor > FLOOR_MAX)
            throw new Exception("Invalid floor");
        
        this.serviceFloors.add(floor);
    }
    private void openDoor(); 
}


class ElevatorManager {
    private List<Elevator> elevators;
    private Map<Integer, Elevator> elevatorMap;
    
    ---------------
    public void processRequest(Request request) {
        if (request.getRequestType() == INSIDE_ELEVATOR) {
            int id = request.getElevatorId();
            if (!elevatorMap.containsKey(id))
                throw Exception("Invalid Elevator ID");
            
            elevatorMap.get(id).addFloor(request.getFloor());
            return;
        }

        // the request is coming from Hall Call.
        int bestElevatorId = selectBestElevator(request);
        elevatorMap.get(bestElevatorId).addFloor(request.getFloor());
        return;
    }

    private void selectBestElevator(Request request) {
        if (request.direction() == UP) {
            int bestElevator = findElevatorGoingUp(request.getFloor());
            if (bestElevator != -1)
                return bestElevator;
            return findBestElevator(request.getFloor());
        }

        int bestElevator = findElevatorGoingDown(request.getFloor());
        if (bestElevator != -1)
            return bestElevator;
        return findBestElevator(request.getFloor());
    }

    private int findBestElevator(int floor) {
        int bestElevator = -1;
        int curMin = 100;

        for (Elevator e : elevators) {
            if (Math.abs(e.getFloor() - floor) < curMin) {
                curMin = Math.abs(e.getFloor() - floor);
                bestElevator = e.getId();
            }
        }
        return bestElevator;
    }

    private int findElevatorGoingUp(int floor) {
        int bestElevator = -1;
        int curMin = 100;

        for (Elevator e : elevators) {
            if (e.getDirection() == UP && e.getFloor() <= floor && (floor - e.getFloor()) < curMin) {
                curMin = floor - e.getFloor();
                bestElevator = e.getId();
            }
        }
        return bestElevator;
    }

    private int findElevatorGoingDown(int floor) {
        int bestElevator = -1;
        int curMin = 100;

        for (Elevator e : elevators) {
            if (e.getDirection() == DOWN && e.getFloor() >= floor && (e.getFloor() - floor) < curMin) {
                curMin = e.getFloor() - floor;
                bestElevator = e.getId();
            }
        }
        return bestElevator;
    }

    public void step(); // calls the step of all the elevators
    
}

class Request {
    int floor;
    Directon direction; // for hall calls it will pass either UP or DOWN
    RequestType requestType;
    private int elevatorId;  // for calls inside the elevator

    ----------
    public Request(int floor, RequestType requestType);
}


// ═══════════════════════════════════════════════════════════════════════════
// EXTENSIBILITY
// ═══════════════════════════════════════════════════════════════════════════

