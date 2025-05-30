import { render, screen, fireEvent, waitFor } from "@testing-library/react"
import ProfileManagementPage from "./page"
import { useToast } from "@/components/ui/use-toast"
import * as apiConfig from "@/lib/api-config" // Mocking the API module

// Mock the useToast hook
jest.mock("@/components/ui/use-toast", () => ({
  useToast: jest.fn().mockReturnValue({ toast: jest.fn() }),
}))

// Mock the API functions
jest.mock("@/lib/api-config", () => ({
  listPerfis: jest.fn(),
  createPerfil: jest.fn(),
  updatePerfil: jest.fn(),
}))

const mockProfiles = [
  { id: 1, nome: "Admin", descricao: "Administrator profile" },
  { id: 2, nome: "User", descricao: "Regular user profile" },
]

describe("ProfileManagementPage", () => {
  beforeEach(() => {
    // Reset mocks before each test
    jest.clearAllMocks();
    (apiConfig.listPerfis as jest.Mock).mockResolvedValue(mockProfiles)
  })

  it("renders the page title", () => {
    render(<ProfileManagementPage />)
    expect(screen.getByText("Gestão de Perfis")).toBeInTheDocument()
  })

  it("renders the 'Adicionar Perfil' button", () => {
    render(<ProfileManagementPage />)
    expect(screen.getByText("Adicionar Perfil")).toBeInTheDocument()
  })

  it("renders the search input", () => {
    render(<ProfileManagementPage />)
    expect(screen.getByPlaceholderText("Buscar perfis...")).toBeInTheDocument()
  })

  it("displays a list of profiles", async () => {
    render(<ProfileManagementPage />)
    await waitFor(() => {
      expect(screen.getByText("Admin")).toBeInTheDocument()
      expect(screen.getByText("User")).toBeInTheDocument()
    })
  })

  it("filters profiles based on search query", async () => {
    render(<ProfileManagementPage />)
    await waitFor(() => { // Ensure initial list is rendered
        expect(screen.getByText("Admin")).toBeInTheDocument();
    });

    fireEvent.change(screen.getByPlaceholderText("Buscar perfis..."), {
      target: { value: "Admin" },
    })

    await waitFor(() => {
      expect(screen.getByText("Admin")).toBeInTheDocument()
      expect(screen.queryByText("User")).not.toBeInTheDocument()
    })
  })

  it("opens the 'Novo Perfil' dialog when 'Adicionar Perfil' button is clicked", () => {
    render(<ProfileManagementPage />)
    fireEvent.click(screen.getByText("Adicionar Perfil"))
    expect(screen.getByText("Novo Perfil")).toBeInTheDocument()
  })

  it("adds a new profile", async () => {
    const newProfile = { id: 3, nome: "Editor", descricao: "Content editor" };
    (apiConfig.createPerfil as jest.Mock).mockResolvedValue(newProfile)

    render(<ProfileManagementPage />)
    fireEvent.click(screen.getByText("Adicionar Perfil"))

    fireEvent.change(screen.getByLabelText("Nome do Perfil"), {
      target: { value: "Editor" },
    })
    fireEvent.change(screen.getByLabelText("Descrição"), {
      target: { value: "Content editor" },
    })
    fireEvent.click(screen.getByText("Salvar"))

    await waitFor(() => {
      expect(apiConfig.createPerfil).toHaveBeenCalledWith({
        nome: "Editor",
        descricao: "Content editor",
      })
      expect(screen.getByText("Editor")).toBeInTheDocument() // Check if new profile is in the list
    })
  })

  it("opens the 'Editar Perfil' dialog when 'Editar' button is clicked", async () => {
    render(<ProfileManagementPage />)
    await waitFor(() => { // Ensure initial list is rendered
        expect(screen.getByText("Admin")).toBeInTheDocument();
    });

    // Get all Edit buttons and click the first one
    const editButtons = screen.getAllByText("Editar");
    fireEvent.click(editButtons[0]);

    expect(screen.getByText("Editar Perfil")).toBeInTheDocument()
    // Check if the form is pre-filled with the correct data
    // @ts-ignore
    expect(screen.getByLabelText("Nome do Perfil").value).toBe("Admin")
  })

  it("updates an existing profile", async () => {
    const updatedProfile = { ...mockProfiles[0], nome: "Super Admin" };
    (apiConfig.updatePerfil as jest.Mock).mockResolvedValue(updatedProfile)

    render(<ProfileManagementPage />)
     await waitFor(() => { // Ensure initial list is rendered
        expect(screen.getByText("Admin")).toBeInTheDocument();
    });

    // Get all Edit buttons and click the first one
    const editButtons = screen.getAllByText("Editar");
    fireEvent.click(editButtons[0]);

    fireEvent.change(screen.getByLabelText("Nome do Perfil"), {
      target: { value: "Super Admin" },
    })
    fireEvent.click(screen.getByText("Salvar Alterações"))

    await waitFor(() => {
      expect(apiConfig.updatePerfil).toHaveBeenCalledWith(mockProfiles[0].id, {
        ...mockProfiles[0],
        nome: "Super Admin",
      })
      expect(screen.getByText("Super Admin")).toBeInTheDocument()
    })
  })
})
