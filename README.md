# <img src="https://github.com/Software-Hardware-Codesign/Automata4j/blob/master/vending-machine-svgrepo-com.svg" width=55 height=55/> Automata4j [![Codacy Badge](https://app.codacy.com/project/badge/Grade/9d1d6ffa15204f13889340e1288ceba8)](https://app.codacy.com/gh/Software-Hardware-Codesign/Automata4j/dashboard?utm_source=gh&utm_medium=referral&utm_content=&utm_campaign=Badge_grade) [![](https://img.shields.io/badge/Automata4j-latest_version-red)](https://github.com/Software-Hardware-Codesign/Automata4j/releases/)
[![](https://github.com/Software-Hardware-Codesign/Automata4j/actions/workflows/build-test.yml/badge.svg)]() [![](https://github.com/Software-Hardware-Codesign/Automata4j/actions/workflows/build-deploy.yml/badge.svg)]()

A finite state automaton API for Java, Android, and jMonkeyEngine Applications.

## Building: 
```bash
┌─[pavl-machine@pavl-machine]─[/home/pavl-machine/projects]
└──╼ $mkdir Automata4j && \
     cd ./Automata4j && \
     git clone https://github.com/Software-Hardware-Codesign/Automata4j.git

┌─[pavl-machine@pavl-machine]─[/home/pavl-machine/projects/Automata4j]
└──╼ $./gradlew automata4j:build && \
     ./gradlew automata4j:generateJavadocJar && \
     ./gradlew automata4j:generateSourcesJar
``` 
## Implementation Example: 
```java
final String version = "incubator"

repositories {
    mavenCentral()
}
dependencies {
    implementation "io.github.software-hardware-codesign:automata4j:${version}"
}
```
```java
public final class SpaceCraftEngine extends Thread implements TransitionalListener {
     private final TransitionalManager transitionalManager = new TransitionalManager();
     private final LatLng initialPosition = VehicleManager.getInstance().getSpaceCraft().getLocation();
     private final SpaceCraftEngine.TravelDistance travelDistance = new SpaceCraftEngine.TravelDistance(10f);
     private final SpaceCraftEngine.MoveCommand horizontalMoveState = new SpaceCraftEngine.MoveCommand();
     
     public SpaceCraftEngine() {
          super(SpaceCraftEngine.class.getName());
     }
     
     /** Run your example from here or a Game state or a Unit Test */
     public static void main(String args[]) {
          final SpaceCraftEngine engine = new SpaceCraftEngine();
          engine.startTravelling();
     }
     
     public void startTravelling() {
          /* Assigns the initial state and starts the transitional manager */
          transitionalManager.assignNextState(horizontalMoveState);
          this.start();
     }
     
     @Override
     public void run() {
          /* Starts the finite-state-system by transiting to the next travel state */
          travel(travelDistance);
     }
     
     @Override
     public <I, O> void onTransition(AutoState<I, O> presentState) {
          final AutoState<PulseCommand, LatLng> autoState = (AutoState<PulseCommand, LatLng>) presentState;
          if (presentState.getStateTracer().getX() > (initialPosition.getX() + 200f)) {
               /* Exits the system once traveling has been completed and docks the spacecraft */
               VehicleManager.getInstance().getSpaceCraft().dock(DockingSpeed.DEFAULT_SPEED);
               Gui.getInstance().message(Message.Type.Alert,
                                   "Completed Travelling 200+ Miles, Docking the SpaceCraft");
               return;
          }
          /* Continues assigning new states as long as the destination has not been met */
          transitionalManager.assignNextState(horizontalMoveState);
          travel(travelDistance);
     }
     
     protected void travel(SpaceCraftEngine.TravelDistance travelDistance) {
          transitionalManager.transit(travelDistance, this);
          Gui.getInstance().message(Message.Type.Alert, "Travelling Now for "
                                   + travelDistance.getPulse() + " Miles");
     }

     protected static class MoveCommand implements AutoState<PulseCommand, LatLng> {
          private PulseCommand command;

          @Override
          public void setInput(PulseCommand command) {
               this.command = command;
          }

          @Override
          public void invoke(PulseCommand command) {
               this.command = command;
               // Your system state goes here
               VehicleManager.getInstance().getSpaceCraft().force(command.getPulse(), 0);
               Gui.getInstance().message(Message.Type.Info, "Added additional "
                                        + command.getPulse() + " Miles");
          }
          
          @Override
          public PulseCommand getInput() {
               return command;
          }
          
          @Override
          public LatLng getStateTracer() {
               return VehicleManager.getInstance().getSpaceCraft().getLocation();
          }
          
          @Override
          public void onFinish() {
               // Your finish code goes here
               VehicleManager.getInstance().getSpaceCraft().stopEngine();
               Gui.getInstance().message(Message.Type.Warning, "Engine Stops");
          }
          
          @Override
          public void onStart() {
               // Your start code goes here
               VehicleManager.getInstance().getSpaceCraft().startEngine();
               Gui.getInstance().message(Message.Type.Info, "Engine Starts");
          }
     }

     protected static class TravelDistance implements PulseCommand {
          private float pulse;
     
          public TravelDistance(float pulse) {
               this.pulse = pulse;
          }
          
          @Override
          public float getPulse() {
               return pulse;
          }
     }

     protected static class Location implements LatLng {
          private float x;
          private float y;
     
          public Location(float x, float y) {
               this.x = x;
               this.y = y;
          }
          
          @Override
          public void setLocation(float x, float y) {
               this.x = x;
               this.y = y;
          }
          
          @Override
          public float getX() {
               return x;
          }
          
          @Override
          public float getY() {
               return y;
          }
     }
}
```

## Appendix
### Features:
- [x] Finite-State-Machine pattern.
- [x] SerialAdder Example.
- [x] API Documentation.
- [x] Document describing the finite-state theory.
- [x] Publishing to Maven for public use.
- [x] Deterministic Finite-State-Automata.
- [ ] Wiki for general use.

### Theory Archive: 
- [Finite-State-Automaton Theory Archive, from Switching and Finite Automata Theory 3rd Edition](https://github.com/Software-Hardware-Codesign/Automata4j/blob/master/archives/Finite-State-Automata.pdf)
- [Finite-State-Recognizers for DFSA V.S. NDFSA](https://github.com/Software-Hardware-Codesign/Automata4j/blob/master/archives/Finite-State-Recognizers(DFSA-NDFSA).pdf)

### For more about Finite-States, find the full TextBook: 
[![](https://github.com/Software-Hardware-Codesign/Automata4j/assets/60224159/d28b39b0-28f3-43e2-859e-787a5e8f88e1)](https://www.amazon.com/Switching-Finite-Automata-Theory-Kohavi/dp/0521857481)

