import { BiImageAdd, BiTrash, BiUpload } from 'react-icons/bi';
import { toast } from 'sonner';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';

import { deleteSystemBoundaryImage, getSystemBoundaryImages, saveSystemBoundaryImage } from '@http/CAST/Step1/SystemBoundaryImage';
import CastSection from '@components/CastSection';
import { useCastConfirm } from '@components/CastConfirm';

interface Props { analysisId: string; }

export default function SystemBoundaryImageForm({ analysisId }: Props) {
    const queryClient = useQueryClient();
    const { confirmDelete, confirmDialog } = useCastConfirm();

    const { data: images, isLoading } = useQuery({
        queryKey: ['system-boundary-images', analysisId],
        queryFn: () => getSystemBoundaryImages(analysisId)
    });

    const currentImage = images && images.length > 0 ? images[0] : null;

    const { mutateAsync: uploadImage, isPending: isUploading } = useMutation({
        mutationFn: async (file: File) => {
            return await saveSystemBoundaryImage(file, analysisId);
        },
        onSuccess: () => {
            toast.success("Boundary Diagram uploaded successfully!");
            queryClient.invalidateQueries({ queryKey: ['system-boundary-images', analysisId] });
        },
        onError: (err: any) => toast.error(err.message || "Error uploading image.")
    });

    const { mutateAsync: removeImage, isPending: isDeleting } = useMutation({
        mutationFn: async (id: string) => {
            return await deleteSystemBoundaryImage(id);
        },
        onSuccess: () => {
            toast.success("Image removed.");
            queryClient.invalidateQueries({ queryKey: ['system-boundary-images', analysisId] });
        },
        onError: (err: any) => toast.error(err.message)
    });

    const handleFileChange = async (e: React.ChangeEvent<HTMLInputElement>) => {
        const file = e.target.files?.[0];
        if (!file) return;

        if (!file.type.startsWith('image/')) {
            toast.error("Please upload a valid image file (PNG, JPG).");
            return;
        }

        await uploadImage(file);

        e.target.value = '';
    };

    return (
        <CastSection title="Boundary Diagram" tooltipInfo="Upload a visual diagram of the system boundary — what is inside and outside the scope of the analysis." hideAddButton={true}>
            {isLoading ? (
                <div style={{ color: 'var(--color-muted-text)', fontSize: '14px', padding: '10px' }}>Loading image...</div>
            ) : (
                <div style={{ display: 'flex', flexDirection: 'column', gap: '16px' }}>

                    {!currentImage ? (
                        <div style={{
                            border: '2px dashed var(--color-gray)',
                            borderRadius: '8px',
                            padding: '40px 20px',
                            textAlign: 'center',
                            backgroundColor: 'var(--color-dark)',
                            display: 'flex',
                            flexDirection: 'column',
                            alignItems: 'center',
                            gap: '12px'
                        }}>
                            <BiImageAdd size={48} color="var(--color-muted-text)" />
                            <div>
                                <p style={{ margin: 0, color: '#d1d5db', fontWeight: 'bold' }}>No boundary diagram has been added yet</p>
                                <p style={{ margin: '4px 0 0 0', color: 'var(--color-muted-text)', fontSize: '12px' }}>Upload a PNG or JPG of the system boundary</p>
                            </div>

                            <label style={{
                                marginTop: '12px',
                                backgroundColor: 'var(--color-yellow)',
                                color: '#000',
                                padding: '8px 16px',
                                borderRadius: '4px',
                                fontWeight: 'bold',
                                cursor: isUploading ? 'not-allowed' : 'pointer',
                                display: 'flex',
                                alignItems: 'center',
                                gap: '8px',
                                opacity: isUploading ? 0.7 : 1
                            }}>
                                <BiUpload size={18} />
                                {isUploading ? 'Uploading...' : 'Choose Image'}
                                <input
                                    type="file"
                                    accept="image/png, image/jpeg"
                                    style={{ display: 'none' }}
                                    onChange={handleFileChange}
                                    disabled={isUploading}
                                />
                            </label>
                        </div>
                    ) : (
                        <div style={{
                            border: '1px solid var(--color-gray)',
                            borderRadius: '8px',
                            overflow: 'hidden',
                            backgroundColor: 'var(--color-dark)',
                            display: 'flex',
                            flexDirection: 'column'
                        }}>
                            <div style={{ display: 'flex', justifyContent: 'space-between', padding: '12px 16px', backgroundColor: 'var(--color-dark)', borderBottom: '1px solid var(--color-gray)' }}>
                                <span style={{ color: '#d1d5db', fontSize: '14px', fontWeight: 'bold' }}>Current Boundary Diagram</span>
                                <button
                                    onClick={async () => { if (await confirmDelete('the System Boundary diagram')) removeImage(currentImage.id); }}
                                    disabled={isDeleting}
                                    style={{ background: 'transparent', border: 'none', color: '#ef4444', cursor: isDeleting ? 'not-allowed' : 'pointer', display: 'flex', alignItems: 'center', gap: '4px', fontSize: '14px' }}
                                >
                                    <BiTrash size={16} /> {isDeleting ? 'Removing...' : 'Remove'}
                                </button>
                            </div>

                            <div style={{ padding: '20px', display: 'flex', justifyContent: 'center' }}>
                                <img
                                    src={currentImage.base64.startsWith('data:image') ? currentImage.base64 : `data:image/png;base64,${currentImage.base64}`}
                                    alt="System Boundary"
                                    style={{ maxWidth: '100%', maxHeight: '600px', objectFit: 'contain', borderRadius: '4px' }}
                                />
                            </div>
                        </div>
                    )}

                </div>
            )}
            {confirmDialog}
        </CastSection>
    );
}
