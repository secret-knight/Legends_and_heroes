// the different tiles that make up a map
public enum Tile {
    Market {
        @Override
        public String toString() {
            return "\u001b[44;1m  M  \u001b[0m";
        }
    }, Inaccessible {
        @Override
        public String toString() {
            return "\u001b[41;1m  I  \u001b[0m";
        }
    }, Common {
        @Override
        public String toString() {
            return "\u001b[42;1m  C  \u001b[0m";
        }
    },
    // these two types of tiles behave same to regular but when occupied, that are colored yellow so the user
    // can identify where the player currently is on the map
    OccupiedMarket {
        @Override
        public String toString() {
            return "\u001b[43;1m XM  \u001b[0m";
        }
    }, OccupiedCommon {
        @Override
        public String toString() {
            return "\u001b[43;1m XC  \u001b[0m";
        }
    };
}
