export interface IPhysicalLossAnalysisInsertDto {
    code: string;
    physicalLossDescription?: string;
    affectedEquipment?: string;
    physicalDesignRequirements?: string;
    physicalControls?: string;
    failuresAndUnsafeInteractions?: string;
    missingOrInadequateControls?: string;
    contextualFactors?: string;
    analysisId: string;
}

export interface IPhysicalLossAnalysisReadDto {
    id: string;
    code: string;
    physicalLossDescription?: string;
    affectedEquipment?: string;
    physicalDesignRequirements?: string;
    physicalControls?: string;
    failuresAndUnsafeInteractions?: string;
    missingOrInadequateControls?: string;
    contextualFactors?: string;
}

export interface IPhysicalLossAnalysisUpdateDto {
    code: string;
    physicalLossDescription?: string;
    affectedEquipment?: string;
    physicalDesignRequirements?: string;
    physicalControls?: string;
    failuresAndUnsafeInteractions?: string;
    missingOrInadequateControls?: string;
    contextualFactors?: string;
}