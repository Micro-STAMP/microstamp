import { BiSearch } from "react-icons/bi";
import styles from "./AnalysisFilters.module.css";

interface AnalysisFiltersProps {
    activeFilter: "All" | "STPA" | "CAST";
    setActiveFilter: (filter: "All" | "STPA" | "CAST") => void;
    searchQuery: string;
    setSearchQuery: (query: string) => void;
    counts: Record<"All" | "STPA" | "CAST", number>; 
}

export default function AnalysisFilters({
    activeFilter,
    setActiveFilter,
    searchQuery,
    setSearchQuery,
    counts
}: AnalysisFiltersProps) {
    const tabs: ("All" | "STPA" | "CAST")[] = ["All", "STPA", "CAST"];

    return (
        <div className={styles.filterBar}>
            <div className={styles.tabsContainer}>
                {tabs.map((tab) => {
                    const isActive = activeFilter === tab;
                    
                    let badgeClass = styles.badgeInactive;
                    if (isActive) {
                        if (tab === "STPA") badgeClass = styles.badgeActiveSTPA;
                        else if (tab === "CAST") badgeClass = styles.badgeActiveCAST;
                        else badgeClass = styles.badgeActiveAll;
                    }

                    return (
                        <button
                            key={tab}
                            onClick={() => setActiveFilter(tab)}
                            className={`${styles.tabBtn} ${isActive ? styles.tabActive : styles.tabInactive}`}
                        >
                            {tab}
                            <span className={`${styles.badge} ${badgeClass}`}>
                                {counts[tab]}
                            </span>
                        </button>
                    );
                })}
            </div>

            <div className={styles.searchContainer}>
                <BiSearch size={16} className={styles.searchIcon} />
                <input
                    type="text"
                    placeholder="Search analyses..."
                    value={searchQuery}
                    onChange={(e) => setSearchQuery(e.target.value)}
                    className={styles.searchInput}
                />
            </div>
        </div>
    );
}