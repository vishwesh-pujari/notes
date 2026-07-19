// ═══════════════════════════════════════════════════════════════════════════
// REQUIREMENTS
//
// Example (Tic Tac Toe):
//   1. Two players alternate placing X and O on a 3x3 grid.
//   2. A player wins by completing a row, column, or diagonal.
//   Out of Scope: UI, AI opponent, networking
// ═══════════════════════════════════════════════════════════════════════════

/*
    Parking Lot:

    Requirements:
        1. There are a fixed set of spots available for parking.
        2. Every spot can accomodate certain vehicle type.
        3. Three Vehicle types need to be supported - Motorcycle, Car, Large Vehicle
        4. System needs to assign a spot based on the vehicle size.
        5. System needs to calculate the fees on exit. Fees are time based and have an hourly rate. Each vehicle type will have the SAME rate per hour. We need to maintain the in-time of every vehicle.
        6. Upon entry, the system will issue a ticket to the car, and it will be validated while exiting and used for fees calculation.
        7. Upon exit, the respective slot needs to be released.
        8. Cross-type parking is not supported. (Motorcycle being parked in large vehicle spot)

    Edge cases:
        1. When all spots for a vehicle type are full, then don't allow the vehicle to enter the parking lot.
        2. If someone tries to exit using an already used ticket, then system should throw an error.

    Out of scope:
        1. Physical gate control
        2. Payment processing.

    Extensibility:
        1. What if at the same time one vehicle tries to enter and one tries to exit
*/


// ═══════════════════════════════════════════════════════════════════════════
// ENTITIES & RELATIONSHIPS
//
// Example (Tic Tac Toe):
//   Game, Board, Player
// ═══════════════════════════════════════════════════════════════════════════

1. Vehicle
2. VehicleType
3. ParkingLotSystem -> stores all spots, fees per hour, assignment and releasing of spots
4. Spot 
5. Ticket -> contains Spot information and entry timestamp


// ═══════════════════════════════════════════════════════════════════════════
// CLASS DESIGN
//
// Example (Tic Tac Toe):
//   class Game:
//     - board: Board
//     - currentPlayer: Player
//     + makeMove(row, col) -> bool
// ═══════════════════════════════════════════════════════════════════════════

class Vehicle:
    - vehicleType : VehicleType
    - vehicleNumber : String

enum VehicleType :
    MOTORCYCLE, CAR, LARGE_VEHICLE

class Spot : 
    - vehicleType : VehicleType
    - available : boolean
    - id : Integer
    - vehicle : Vehicle // can be null if nothing is parked

    + bookSpot(Vehicle vehicle)
    + releaseSpot()

class Ticket : 
    - vehicle : Vehicle
    - spot : Spot
    - id : Integer
    - valid : boolean
    - timeStamp : TimeStamp
    - AtomicInteger ticketSequence; // next available ticket number 


    + Ticket(Spot spot, Vehicle vehicle)
    + getFees(TimeStamp exitTimestamp)
    + invalidate()

class ParkingLotSystem : 
    - Spot[] spots; // fixed size spots, hence an array
    - Map<Integer, Ticket> tickets; // list of tickets issued by system
    - int ticketSequence; // next available ticket number
    - FeesStrategy feesStrategy; // used while exiting
    
    + entry(Vehicle vehicle) -> assigns spot if available
    + exit(Ticket ticket) -> calculates fees and releases the spot

interface FeesStrategy :
    + calculateFees()

class PerHourFeesStrategy implements FeesStrategy:
    - static final perHourRate : Integer
    + calculateFees();


// ═══════════════════════════════════════════════════════════════════════════
// IMPLEMENTATION
// ═══════════════════════════════════════════════════════════════════════════

class Spot {
    private static final VehicleType vehicleType;
    private boolean available;
    private Vehicle vehicle; // will be null if no vehicle exists on the spot

    public void bookSpot(Vehicle vehicle) {
        if (!available)
            throw new RuntimeException("Spot is not available");
        
        available = false;
        this.vehicle = vehicle;
    }

    public void releaseSpot() {
        available = true;
        vehicle = null;
    }
}

class Ticket {
    private Vehicle vehicle;
    private Spot spot;
    private int id;
    private boolean valid;
    private TimeStamp createTimeStamp;
    private static AtomicInteger ticketSequence; // next available ticket number 

    static {
        ticketSequence = new AtomicInteger(1);
    }

    public Ticket(Spot spot, Vehicle vehicle) {
        this.spot = spot;
        this.vehicle = vehicle;
        this.id = ticketSequence.getAndIncrement();
        this.valid = true;
        this.createTimeStamp = currentTimestamp();
    }

    public int getId() {
        return this.id;
    }

    public void invalidate() {
        this.valid = false;
    }

    public TimeStamp getCreateTimeStamp() {
        return this.createTimeStamp;
    }
}

interface FeesStrategy {
    float calculateFees(TimeStamp entry, TimeStamp exit);
}

class PerHourFeesStrategy implements FeesStrategy {
    private static final perHourRate = 4;
    public float calculateFees(TimeStamp entry, TimeStamp exit) {
        return Math.ceil(getHours(exit, entry)) * perHourRate;
    }
}

interface AllocationStrategy {
    Spot getAvailableSpot(VehicleType vehicleType);
}

class Floor {
    private Spot[] spots;
    private AllocationStrategy;
}

class ParkingLotSystem {
    private Floor[] floors; // fixed size spots, hence an array
    private Map<Integer, Ticket> tickets; // list of tickets issued by system
    
    private FeesStrategy feesStrategy; // used while exiting

    public ParkingLotSystem(int numSpots, FeesStrategy feesStrategy) {
        spots = new Spot[numSpots];
        this.feesStrategy = feesStrategy;
        tickets = new HashMap<>();
    }

    public Ticket entry(Vehicle vehicle) {
        Spot availableSpot = getAvailableSpot(vehicle);
        if (availableSpot == null) {
            throw new RuntimeException("Parking is full for the vehicle type");
        }

        availableSpot.bookSpot(vehicle);
        Ticket ticket = new Ticket(availableSpot, vehicle);
        tickets.put(ticket.getId(), ticket);
        return ticket;
    }

    private Spot getAvailableSpot(Vehicle vehicle) {
        for (Floor floor : floors) { // go from the lowest to the highest floor
            Spot spot = floor.allocationStrategy.getAvailableSpot();
            if (spot == null)
                continue;
            
            return spot;
            
        }
        return null;
    }

    public float exit(Ticket ticket) {
        if (!ticket.isValid()) {
            throw new RuntimeException("Ticket is invalid / already used");
        }

        Spot spot = ticket.getSpot();
        spot.releaseSpot();
        
        
        float fees = feesStrategy.calculateFees(ticket.getCreateTimeStamp(), currentTimeStamp());
        ticket.invalidate();
        return fees;
    }
}


// ═══════════════════════════════════════════════════════════════════════════
// EXTENSIBILITY
// ═══════════════════════════════════════════════════════════════════════════

